package com.ajvm.graphqlserver.graphql

import com.ajvm.graphqlserver.db.DBRepositoryInsertions
import com.ajvm.graphqlserver.db.DBRepositoryRetrieval
import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import com.expediagroup.graphql.server.operations.Mutation
import graphql.schema.DataFetchingEnvironment
import org.springframework.stereotype.Component

@Component
class Mutations(val queries: Queries, val dbInsertions: DBRepositoryInsertions, val dbRetrieval: DBRepositoryRetrieval) {
    fun updateCustomer(id: Int, name: String?=null, email: String?=null) : Boolean {
        dbInsertions.updateCustomer(id, name, email)
        return true
    }
    fun addNewProduct(name: String, description: String, price: Int): Product {
        val productInfo = dbInsertions.insertProduct(name, description, price)
        return Product(productInfo.id, name, description, price)
    }
    fun addToShoppingCart(customerId: Int, productId: Int, amount: Int): ProductAndAmount {
        val dd = dbInsertions.insertShoppingCart(customerId, productId, amount)
        return ProductAndAmount(queries, productId, amount)
    }
    fun placeOrder(customerId: Int) : Order {
        val orderId = dbInsertions.placeOrder(customerId)
        return Order(queries, orderId, false)
    }
    fun registerUser(name: String, email: String, password: String): String {
        if (dbRetrieval.findCustomers(null, email).isNotEmpty()) {
            return "Email Taken"
        }
        dbInsertions.insertCustomer(name, email, hash(password)).let{
            return "Registered"
        }
    }
}