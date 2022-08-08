package com.ajvm.graphqlserver.db

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.CachePut
import org.springframework.stereotype.Repository

sealed class DBEvent {
    data class CustomerInsertion(val id: Int): DBEvent()
    data class ProductInsertion(val id: Int, val name: String, val description: String, val price: Int): DBEvent()
    data class OrderInsertion(val id: Int): DBEvent()
}

@Repository
class DBRepositoryInsertionsImpl(val dbInstanceImpl: DBInstance) : DBRepositoryInsertions {
    val db = Channel<DBEvent>()
    fun signalEvent(event: DBEvent) {
        CoroutineScope(Dispatchers.IO).launch {
            db.send(event)
        }
    }
    @CachePut(value = ["Product"], key="#result.id")
    @CacheEvict(value = ["ProductIDS"], allEntries = true)
    override fun insertProduct(name: String, description: String, price: Int): ProductInfo {
        val transaction =  transaction {
            ProductTable.insert {
                it[ProductTable.name] = name
                it[ProductTable.description] = description
                it[ProductTable.price] = price
            } get ProductTable.id
        }
        signalEvent(DBEvent.ProductInsertion(transaction, name, description, price))
        return ProductInfo(transaction, name, description, price)
    }
    @CachePut(value = ["Customer"], key="#result.id")
    @CacheEvict(value = ["CustomerIDS"], allEntries = true)
    override fun insertCustomer(name: String, email: String, password:String): CustomerInfo {
        return transaction {
            val result = CustomerTable.insert {
                it[CustomerTable.name] = name
                it[CustomerTable.email] = email
                it[CustomerTable.password] = password
            } get CustomerTable.id
            CustomerInfo(result, name, email)
        }

    }
    @CacheEvict(value = ["ShoppingCart"], key = "#root.args[0]")
    override fun insertShoppingCart(customerId: Int, productId: Int, amount: Int): ProductAndAmountInfo {
        transaction {
            ShoppingCartTable.insert {
                it[ShoppingCartTable.customer] = customerId
                it[ShoppingCartTable.product] = productId
                it[ShoppingCartTable.amount] = amount
            } get ShoppingCartTable.customer
        }
        return ProductAndAmountInfo(productId, amount)
    }
    override fun insertOrder(customerId: Int): Int {
        return transaction {
            OrderTable.insert {
                it[OrderTable.customer] = customerId
                it[OrderTable.payed] = false
            } get OrderTable.id
        }
    }
    override fun insertOrderToProducts(orderId: Int, productId: Int, amount: Int): Int {
        return transaction {
            OrderToProducts.insert {
                it[OrderToProducts.order] = orderId
                it[OrderToProducts.product] = productId
                it[OrderToProducts.amount] = amount
            } get OrderToProducts.order
        }
    }

    override fun emptyShoppingCart(customerId: Int) {
        ShoppingCartTable.deleteWhere{ ShoppingCartTable.customer eq customerId }
    }
    @CachePut(value = ["Customer"], key="#root.args[0]")
    override fun updateCustomer(id: Int, name: String?, email: String?) {
        return transaction {
            CustomerTable.update({ CustomerTable.id eq id }) { result ->
                name?.let { result[CustomerTable.name] = name }
                email?.let { result[CustomerTable.email] = email }
            }
        }
    }
    @CacheEvict(value = ["ShoppingCart", "OrderIds"], key = "#root.args[0]")
    override fun placeOrder(customerId: Int): Int {
        val orderId = insertOrder(customerId)
        transaction {
            ShoppingCartTable.select { ShoppingCartTable.customer eq customerId }.forEach { shoppingCartItem ->
                insertOrderToProducts(orderId, shoppingCartItem[ShoppingCartTable.product], shoppingCartItem[ShoppingCartTable.amount])
            }
            emptyShoppingCart(customerId)
        }
        return orderId
    }

}

