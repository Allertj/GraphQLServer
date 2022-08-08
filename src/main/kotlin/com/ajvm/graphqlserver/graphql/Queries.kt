package com.ajvm.graphqlserver.graphql

import com.ajvm.graphqlserver.db.DBRepositoryRetrieval
import com.ajvm.graphqlserver.db.ProductAndAmountInfo
import org.springframework.stereotype.Component

@Component
class Queries(val dbRetrieval: DBRepositoryRetrieval, val tokenManager: TokenManager) {
    fun getProduct(productId: Int): Product? {
        return dbRetrieval.getProduct(productId)?.let {
            Product(it.id, it.name, it.description, it.price)
        }
    }
    fun findProduct(name: String?, price: Int?) : List<Product> {
        return dbRetrieval.findProduct(name, price)
            .map { Product(it.id, it.name, it.description, it.price) }
    }
    fun getAllProducts() : List<Product>{
        return dbRetrieval.getAllProducts()
            .mapNotNull { it.let { it1 -> getProduct(it1) } }
    }
    fun getCustomer(customerId: Int) : Customer? {
        return dbRetrieval.getCustomerInfo(customerId)?.let {
            Customer(this, it.id, it.name, it.email)
        }
    }
    fun shoppingCart(customerId: Int): List<ProductAndAmount> {
        val info = dbRetrieval.getShoppingCart(customerId)
        return info.mapNotNull { productInfo ->
            dbRetrieval.getProduct(productInfo.id)?.let {
                ProductAndAmount(this, it.id, productInfo.amount)
            }
        }
    }
    fun getAllCustomers(): List<Customer> {
        return dbRetrieval.getAllCustomers()
            .mapNotNull { dbRetrieval.getCustomerInfo(it.id) }
            .map { Customer(this, it.id, it.name, it.email) }
    }

    fun findCustomers(name: String?, email: String?) : List<Customer> {
        return dbRetrieval.findCustomers(name, email)
            .mapNotNull { dbRetrieval.getCustomerInfo(it.id) }
            .map { Customer(this, it.id, it.name, it.email) }
    }
    fun getOrdersFromCustomer(customerId: Int) : List<Order> {
        return dbRetrieval.getOrders(customerId).mapNotNull {
            getOrder(it)
        }
    }
    fun getProductsFromOrder(orderId: Int): List<ProductAndAmount> {
        return dbRetrieval.getProductsFromOrder(orderId)
            .mapNotNull { it?.let { it1 -> getProduct(it1.id)?.let { it2 -> ProductAndAmount(this, it1.id, it1.amount) } } }
    }
    fun getOrder(orderId: Int): Order? {
        val order = dbRetrieval.getOrder(orderId)
        order?.let {
            val products = dbRetrieval.getProductsFromOrder(orderId)
                .mapNotNull { it?.let { it1 -> getProduct(it1.id)?.let { it2 -> ProductAndAmount(this, it1.id, it1.amount) } } }
             return Order(this, orderId, order.payed)
        }
        return null
    }
    fun loginUser(email: String, password: String): String? {
        if (dbRetrieval.checkHash(email, hash(password))) {
            return tokenManager.generateJwtToken(email)
        }
        return null
    }

}