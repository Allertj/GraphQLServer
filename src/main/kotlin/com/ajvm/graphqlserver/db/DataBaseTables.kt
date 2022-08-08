package com.ajvm.graphqlserver.db

import org.jetbrains.exposed.sql.Table

object CustomerTable : Table() {
    val id = integer("id").autoIncrement()
    val name = (varchar("first_name", 50))
    val email = (varchar("email", 50))
    val password  = (varchar("password", 100))
    override val primaryKey = PrimaryKey(id)
}

object ShoppingCartTable : Table() {
    val customer = (integer("customer"))
        .references(CustomerTable.id)
    val product = (integer("product"))
        .references(ProductTable.id)
    val amount = (integer("amount"))
    override val primaryKey = PrimaryKey(customer, product)
}

object OrderToProducts : Table() {
    val order = (integer("order"))
        .references(OrderTable.id)
    val product = (integer("product"))
        .references(ProductTable.id)
    val amount = (integer("amount"))
    override val primaryKey = PrimaryKey(order, product)
}

object OrderTable : Table() {
    val id = integer("id").autoIncrement()
    val payed = bool("payed").default(false)
    val customer = (integer("customer"))
        .references(CustomerTable.id)
    override val primaryKey = PrimaryKey(id)
}

object ProductTable : Table() {
    val id = integer("id").autoIncrement()
    val description = (varchar("description", 20))
    val name = (varchar("name", 20))
    val price = integer("price")
    override val primaryKey = PrimaryKey(id)
}
