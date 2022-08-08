package com.ajvm.graphqlserver.graphql

import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import com.expediagroup.graphql.server.operations.Query
import graphql.schema.DataFetchingEnvironment
import org.springframework.stereotype.Component

@Component
class ExposedQueries(val queries: Queries, val tokenManager: TokenManager): Query {
    fun loginUser(email: String, password: String): String? {
        return queries.loginUser(email, password)
    }
    fun getProduct(@GraphQLIgnore dfe: DataFetchingEnvironment, productId: Int): Product? {
        return if (tokenManager.verification(dfe)) queries.getProduct(productId) else null
    }
    fun findProduct(@GraphQLIgnore dfe: DataFetchingEnvironment, name: String?, price: Int?) : List<Product>? {
        return if  (tokenManager.verification(dfe)) queries.findProduct(name, price) else null
    }
    fun getAllProducts(@GraphQLIgnore dfe: DataFetchingEnvironment) : List<Product>? {
        return if  (tokenManager.verification(dfe)) queries.getAllProducts() else null
    }
    fun getCustomer(@GraphQLIgnore dfe: DataFetchingEnvironment, customerId: Int): Customer? {
        return if  (tokenManager.verification(dfe)) queries.getCustomer(customerId) else null
    }
    fun getShoppingCart(@GraphQLIgnore dfe: DataFetchingEnvironment, customerId: Int): List<ProductAndAmount>? {
        return if  (tokenManager.verification(dfe)) queries.shoppingCart(customerId) else null
    }
    fun getAllCustomers(@GraphQLIgnore dfe: DataFetchingEnvironment): List<Customer>? {
        return if  (tokenManager.verification(dfe)) queries.getAllCustomers() else null
    }
    fun findCustomer(@GraphQLIgnore dfe: DataFetchingEnvironment, name: String?, email: String?): List<Customer>? {
        return if  (tokenManager.verification(dfe)) queries.findCustomers(name, email) else null
    }
    fun getOrdersFromCustomer(@GraphQLIgnore dfe: DataFetchingEnvironment, customerId: Int): List<Order>? {
        return if  (tokenManager.verification(dfe)) queries.getOrdersFromCustomer(customerId) else null
    }
    fun getProductsFromOrder(@GraphQLIgnore dfe: DataFetchingEnvironment, orderId: Int): List<ProductAndAmount>? {
        return if  (tokenManager.verification(dfe)) queries.getProductsFromOrder(orderId) else null
    }
    fun getOrder(@GraphQLIgnore dfe: DataFetchingEnvironment, orderId: Int): Order? {
        return if  (tokenManager.verification(dfe)) queries.getOrder(orderId) else null
    }

}
