package com.hands8142.discord.Util

import com.hands8142.discord.dotenv
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

private val databaseUrl = dotenv["DATABASE_URL"]
private val databaseUser = dotenv["DATABASE_USER"]
private val databasePassword = dotenv["DATABASE_PASSWORD"]

object Database {
    fun connection(): Connection? {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver")
            val connect = DriverManager.getConnection(databaseUrl, databaseUser, databasePassword)
            println("Database Connection Completed")
            return connect
        } catch (e: SQLException) {
            e.printStackTrace()
        }
        return null
    }

    fun createTable(): Connection? {
        val database = connection()
        if (database !== null) {
            try {
                val sql =
                    "CREATE TABLE level (guildId BIGINT NOT NULL, userId BIGINT NOT NULL, xp BIGINT NOT NULL, level BIGINT NOT NULL)"
                if (database.metaData.getColumns(null, null, "level", null).next()) {
                    val drop_sql = "DROP TABLE level"
                    database.prepareStatement(drop_sql).executeUpdate()
                    database.prepareStatement(sql).executeUpdate()
                    println("Drop Table And Create Table Completed")
                } else {
                    database.prepareStatement(sql).executeUpdate()
                    println("Create Table Completed")
                }
                return database
            } catch (e: SQLException) {
                e.printStackTrace()
            }
        }
        return null
    }
}