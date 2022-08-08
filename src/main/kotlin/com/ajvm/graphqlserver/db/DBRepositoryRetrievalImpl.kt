package com.ajvm.graphqlserver.db

import com.fasterxml.jackson.annotation.JsonProperty
import graphql.execution.SimpleDataFetcherExceptionHandler
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Repository
import java.util.NoSuchElementException
import kotlinx.serialization.Serializable
import org.springframework.cache.annotation.CachePut
import org.springframework.context.annotation.Bean


data class CustomerInfo (@JsonProperty("id") val id: Int,
                         @JsonProperty("name") var name: String,
                         @JsonProperty("email") var email: String)

data class OrderInfo(@JsonProperty("id") val id: Int,
                     @JsonProperty("payed") val payed: Boolean,
                     @JsonProperty("customerId") val customerId: Int)

data class ProductAndAmountInfo(@JsonProperty("id") val id: Int,
                                @JsonProperty("amount") val amount: Int)

data class ProductInfo(@JsonProperty("id") val id: Int,
                       @JsonProperty("name") val name: String,
                       @JsonProperty("description") val description: String,
                       @JsonProperty("price") val price: Int)

@Repository
class DBRepositoryRetrievalImpl(val dbInstance: DBInstance) : DBRepositoryRetrieval {
    @Cacheable(cacheNames = ["CustomerIDS"])
    override fun getAllCustomers(): List<CustomerInfo> {
        return transaction {
            CustomerTable.selectAll().map {
                CustomerInfo(it[CustomerTable.id],
                    it[CustomerTable.name],
                    it[CustomerTable.email])
            }
        }
    }
    @Cacheable(cacheNames = ["SearchCustomers"])
    override fun findCustomers(name: String?, email: String?): List<CustomerInfo> {
        return transaction {
            val emailSearch = email?.let { CustomerTable.email eq email }
            val nameSearch = name?.let { CustomerTable.name eq name }
            val search = listOf(emailSearch, nameSearch)
                .filterNotNull()
                .reduce { first, second -> first.and(second) }
            CustomerTable.select(search).map {
                CustomerInfo(it[CustomerTable.id],
                             it[CustomerTable.name],
                             it[CustomerTable.email])
                }
            }
    }
    @Cacheable(cacheNames = ["SearchProducts"])
    override fun findProduct(name: String?, price: Int?): List<ProductInfo> {
        return transaction {
            val priceSearch = price?.let { ProductTable.price eq price }
            val nameSearch = name?.let { ProductTable.name eq name }
            val search = listOf(priceSearch, nameSearch)
                .filterNotNull()
                .reduce { first, second -> first.and(second) }
            ProductTable.select(search).map {
                ProductInfo(it[ProductTable.id],
                    it[ProductTable.name],
                    it[ProductTable.description],
                    it[ProductTable.price])
            }
        }
    }
    @Cacheable(cacheNames = ["Customer"])
    override fun getCustomerInfo(customerId: Int): CustomerInfo? {
        return transaction {
            try {
                CustomerTable.select { CustomerTable.id eq customerId }.first().run {
                    CustomerInfo(
                        this[CustomerTable.id],
                        this[CustomerTable.name],
                        this[CustomerTable.email]
                    )
                }
            } catch (e: NoSuchElementException) {
                null
            }
        }
    }
    @Cacheable(cacheNames = ["Order"])
    override fun getOrder(orderId: Int): OrderInfo? {
        return transaction {
            try {
                OrderTable.select { OrderTable.id eq orderId }.first().run {
                    OrderInfo(
                        this[OrderTable.id],
                        this[OrderTable.payed],
                        this[OrderTable.customer]
                    )
                }
            } catch (e: NoSuchElementException) {
                null
            }
        }
    }
    @Cacheable(cacheNames = ["ProductsFromOrder"])
    override fun getProductsFromOrder(orderId: Int): List<ProductAndAmountInfo> {
        return transaction {
            OrderToProducts.select { OrderToProducts.order eq orderId }.map {
                ProductAndAmountInfo(it[OrderToProducts.product], it[OrderToProducts.amount])
            }
        }
    }
    @Cacheable(cacheNames = ["OrderIds"])
    override fun getOrders(customerId: Int): List<Int> {
        return transaction {
            OrderTable.select { OrderTable.customer eq customerId }.map {
                it[OrderTable.id]
            }
        }
    }
    @Cacheable(cacheNames = ["ShoppingCart"])
    override fun getShoppingCart(customerId: Int): List<ProductAndAmountInfo> {
        return transaction {
            ShoppingCartTable.select { ShoppingCartTable.customer eq customerId }.map {
                ProductAndAmountInfo(it[ShoppingCartTable.product], it[ShoppingCartTable.amount])
            }
        }
    }
    @Cacheable(cacheNames = ["Product"])
    override fun getProduct(productId: Int): ProductInfo? {
        return transaction {
            try {
                ProductTable.select { ProductTable.id eq productId }.first().let {
                    ProductInfo(
                        it[ProductTable.id],
                        it[ProductTable.name],
                        it[ProductTable.description],
                        it[ProductTable.price]
                    )
                }
            } catch (e: NoSuchElementException) {
                null
            }
        }
    }
//    @Cacheable(cacheNames = ["ProductIDS"])
    override fun getAllProducts(): List<Int> {
        return transaction {
            ProductTable.selectAll().map {
                it[ProductTable.id]
            }
        }
    }
    override fun checkHash(email: String, hash: String): Boolean {
        return transaction{
            try {
                CustomerTable.select { CustomerTable.email eq email }.first().let {
                    it[CustomerTable.password] == hash
                }
            } catch (e: NoSuchElementException) {
                false
            }
        }
    }
}