package com.ajvm.graphqlserver.db

interface DBRepositoryRetrieval {
    fun getAllCustomers(): List<CustomerInfo>
    fun getCustomerInfo(customerId: Int): CustomerInfo?
    fun getOrder(orderId: Int): OrderInfo?
    fun getOrders(customerId: Int): List<Int>
    fun getShoppingCart(customerId: Int): List<ProductAndAmountInfo>
    fun getProduct(productId: Int): ProductInfo?
    fun getAllProducts(): List<Int>
    fun checkHash(email: String, hash: String) : Boolean
    fun findCustomers(name: String?, email: String?): List<CustomerInfo>
    fun findProduct(name: String?, price: Int?) : List<ProductInfo>
    fun getProductsFromOrder(orderId: Int): List<ProductAndAmountInfo?>
}
