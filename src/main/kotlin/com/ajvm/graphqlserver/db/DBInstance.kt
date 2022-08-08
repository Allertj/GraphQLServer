package com.ajvm.graphqlserver.db

import org.jetbrains.exposed.sql.Database

interface DBInstance {
    val dbUrl : String
    val dbPass : String
    val dbUser: String
    var database : Database
    fun addTables()
    fun dropTables()
}