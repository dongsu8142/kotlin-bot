package com.hands8142.discord.Util

import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

private val databaseConfig = Config.getConfig().database
private val databaseHost = databaseConfig.host
private val databasePort = databaseConfig.port
private val databaseDatabase = databaseConfig.database
private val databaseUsername = databaseConfig.username
private val databasePassword = databaseConfig.password
private val ddlAuto = databaseConfig.ddl_auto

object Database {
    fun connection(): Connection? {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver")
            val connect = DriverManager.getConnection("jdbc:mysql://$databaseHost:$databasePort/$databaseDatabase", databaseUsername, databasePassword)
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
