package com.ajvm.graphqlserver.db

interface DBRepositoryInsertions {
    fun insertCustomer(name: String, email: String, password: String): CustomerInfo?
    fun insertProduct(name: String, description: String, price: Int): ProductInfo
    fun insertShoppingCart(customerId: Int, productId: Int, amount: Int): ProductAndAmountInfo
    fun insertOrder(customerId: Int): Int
    fun insertOrderToProducts(orderId: Int, productId: Int, amount: Int): Int
    fun emptyShoppingCart(customerId: Int)
    fun updateCustomer(id: Int, name: String?, email: String?)
    fun placeOrder(customerId: Int): Int

}