package com.ajvm.graphqlserver.graphql

import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import com.expediagroup.graphql.server.operations.Mutation
import graphql.schema.DataFetchingEnvironment
import org.springframework.stereotype.Component

@Component
class ExposedMutations(val mutations: Mutations, val tokenManager: TokenManager) : Mutation {
    fun updateCustomer(@GraphQLIgnore dfe: DataFetchingEnvironment, id: Int, name: String?=null, email: String?=null): Boolean? {
        return if  (tokenManager.verification(dfe)) mutations.updateCustomer(id, name, email) else null
    }
    fun addNewProduct(@GraphQLIgnore dfe: DataFetchingEnvironment, name: String, description: String, price: Int): Product? {
        return if  (tokenManager.verification(dfe)) mutations.addNewProduct(name, description, price) else null
    }
    fun addToShoppingCart(@GraphQLIgnore dfe: DataFetchingEnvironment, customerId: Int, productId: Int, amount: Int): ProductAndAmount? {
        return if  (tokenManager.verification(dfe)) mutations.addToShoppingCart(customerId, productId, amount) else null
    }
    fun placeOrder(@GraphQLIgnore dfe: DataFetchingEnvironment, customerId: Int): Order? {
        return if  (tokenManager.verification(dfe)) mutations.placeOrder(customerId) else null
    }
    fun registerUser(name: String, email: String, password: String): String {
        return mutations.registerUser(name, email, password)
    }
}
