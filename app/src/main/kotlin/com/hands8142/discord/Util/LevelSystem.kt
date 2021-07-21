package com.hands8142.discord.Util

import dev.kord.common.entity.Snowflake
import dev.kord.core.Kord
import java.sql.ResultSet
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.math.floor
import kotlin.math.sqrt

private val database = Database.connection()

object LevelSystem {
    fun checkDatabase(): Boolean {
        return database !== null
    }

    fun appendXp(userId: Long, guildId: Long, xp: Long): Boolean {
        if (database !== null) {
            val sql =
                "SELECT * from level WHERE guildId = $guildId AND userId = $userId"
            val user = database.prepareStatement(sql).executeQuery()
            if (user.next()) {
                val newXp = user.getLong("xp") + xp
                val newLevel = floor(0.1 * sqrt(newXp.toDouble())).toLong()
                val updateSql =
                    "UPDATE level SET level = $newLevel, xp = $newXp WHERE guildId = $guildId AND userId = $userId"
                database.prepareStatement(updateSql).executeUpdate()
                return floor(0.1 * sqrt(user.getLong("xp").toDouble())) < newLevel
            } else {
                val insertSql =
                    "INSERT INTO level(userId, guildId, xp, level) VALUES ($userId, $guildId, $xp, ${
                    floor(
                        0.1 * sqrt(xp.toDouble())
                    ).toLong()
                    })"
                database.prepareStatement(insertSql).executeUpdate()
                return floor(0.1 * sqrt(xp.toDouble())) > 0
            }
        }
        return false
    }

    fun fetch(userId: Long, guildId: Long): ResultSet? {
        if (database !== null) {
            val sql =
                "SELECT * from level WHERE guildId = $guildId AND userId = $userId"
            return database.prepareStatement(sql).executeQuery()
        }
        return null
    }

    fun fetchLeaderboard(guildId: Long, limit: Int): List<HashMap<String, Any>>? {
        if (database !== null) {
            val sql = "SELECT * from level WHERE guildId = $guildId ORDER BY xp DESC"
            val users = database.prepareStatement(sql).executeQuery()
            val usersList = convertResultSetToArrayList(users)
            return if (usersList.size > limit) {
                usersList.slice(0 until limit)
            } else {
                usersList
            }
        }
        return null
    }

    suspend fun computeLeaderboard(
        kord: Kord,
        leaderboard: List<HashMap<String, Any>>
    ): ArrayList<HashMap<String, Any>> {
        val computedArray: ArrayList<HashMap<String, Any>> = ArrayList()

        leaderboard.map {
            val row = HashMap<String, Any>()
            val username =
                if (kord.getUser(Snowflake(it["userId"].toString().toLong()))?.username !== null) kord.getUser(
                    Snowflake(
                        it["userId"].toString().toLong()
                    )
                )?.username!! else "Unknown"
            val discriminator =
                if (kord.getUser(Snowflake(it["userId"].toString().toLong()))?.username !== null) kord.getUser(
                    Snowflake(
                        it["userId"].toString().toLong()
                    )
                )?.discriminator!! else "0000"
            row["guildId"] = it["guildId"]!!
            row["userId"] = it["userId"]!!
            row["xp"] = it["xp"]!!
            row["level"] = it["level"]!!
            row["position"] = leaderboard.indexOf(it) + 1
            row["username"] = username
            row["discriminator"] = discriminator
            computedArray.add(row)
        }

        return computedArray
    }

    fun xpFor(targetLevel: Long): Long {
        return targetLevel * targetLevel * 100
    }

    private fun convertResultSetToArrayList(rs: ResultSet): ArrayList<HashMap<String, Any>> {
        val md = rs.metaData
        val columns = md.columnCount
        val list = ArrayList<HashMap<String, Any>>()

        while (rs.next()) {
            val row = HashMap<String, Any>(columns)
            for (i in 1..columns) {
                row[md.getColumnName(i)] = rs.getObject(i)
            }
            list.add(row)
        }
        return list
    }
}
