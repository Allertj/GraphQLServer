package com.ajvm.graphqlserver.graphql

import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import com.fasterxml.jackson.annotation.JsonProperty

data class Product(@JsonProperty("id") val id: Int,
                   @JsonProperty("name") val name: String,
                   @JsonProperty("description") val description: String,
                   @JsonProperty("price") val price: Int
)

data class ProductAndAmount(
    @GraphQLIgnore
    val queries: Queries,
    @JsonProperty("id") val id: Int,
    @JsonProperty("amount") val amount: Int) {
    @JsonProperty("product") fun product(): Product? {
        return queries.getProduct(id)
    }
}

class Order(
    @GraphQLIgnore
    val queries: Queries,
    val id: Int,
    val payed: Boolean) {
    fun items(): List<ProductAndAmount> {
        return queries.getProductsFromOrder(id)
    }
}

class Customer(
    @GraphQLIgnore
    val queries: Queries,
    val id: Int,
    val name: String,
    var email: String ) {
    fun orders(): List<Order> {
        return queries.getOrdersFromCustomer(id)
    }
    fun shoppingCart(): List<ProductAndAmount> {
        return queries.shoppingCart(id)
    }

}

