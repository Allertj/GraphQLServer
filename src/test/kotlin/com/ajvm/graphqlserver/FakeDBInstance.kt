package com.ajvm.graphqlserver

import com.ajvm.graphqlserver.db.*
import io.github.cdimascio.dotenv.dotenv
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction

class FakeDBInstance : DBInstance {
    val dotenv = dotenv {
        directory = "./"
    }
    override val dbUrl = "jdbc:postgresql://${dotenv["SERVER"]}:${dotenv["DBPORT"]}/${dotenv["DBNAME"]}"
    override val dbUser: String = dotenv["DBUSER"]
    override val dbPass: String = dotenv["DBPASS"]
    override var database: Database = Database.connect(
        dbUrl, driver = "org.postgresql.Driver",
        user = dbUser, password = dbPass
    )
    init {
        transaction {
            addLogger(StdOutSqlLogger)
            addTables()
        }
    }
    override fun addTables() {
        SchemaUtils.create(CustomerTable)
        SchemaUtils.create(ProductTable)
        SchemaUtils.create(ShoppingCartTable)
        SchemaUtils.create(OrderTable)
        SchemaUtils.create(OrderToProducts)
    }
    override fun dropTables() {
        SchemaUtils.drop(CustomerTable)
        SchemaUtils.drop(ProductTable)
        SchemaUtils.drop(ShoppingCartTable)
        SchemaUtils.drop(OrderToProducts)
    }
}