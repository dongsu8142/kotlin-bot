package com.hands8142.discord.Util

import com.hands8142.discord.dotenv
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

private val databaseUrl = dotenv["DATABASE_URL"]
private val databaseUser = dotenv["DATABASE_USER"]
private val databasePassword = dotenv["DATABASE_PASSWORD"]
private val ddlAuto = dotenv["DDL-AUTO"]

object Database {
    fun connection(): Connection? {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver")
            val connect = DriverManager.getConnection(databaseUrl, databaseUser, databasePassword)
            println("Database Connection Completed")
            if (ddlAuto == "create") {
                createTable(connect)
            }
            return connect
        } catch (e: SQLException) {
            e.printStackTrace()
        }
        return null
    }

    private fun createTable(database: Connection?) {
        if (database !== null) {
            try {
                val sql =
                    "CREATE TABLE level (guildId BIGINT NOT NULL, userId BIGINT NOT NULL, xp BIGINT NOT NULL, level BIGINT NOT NULL)"
                if (database.metaData.getColumns(null, null, "level", null).next()) {
                    val dropSql = "DROP TABLE level"
                    database.prepareStatement(dropSql).executeUpdate()
                    database.prepareStatement(sql).executeUpdate()
                    println("Drop Table And Create Table Completed")
                } else {
                    database.prepareStatement(sql).executeUpdate()
                    println("Create Table Completed")
                }
            } catch (e: SQLException) {
                e.printStackTrace()
            }
        }
    }
}