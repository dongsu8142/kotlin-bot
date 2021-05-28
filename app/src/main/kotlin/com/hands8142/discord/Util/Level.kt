package com.hands8142.discord.Util

import com.hands8142.discord.database
import java.sql.ResultSet
import kotlin.math.floor
import kotlin.math.sqrt

fun appendXp(userId: Long, guildId: Long, xp: Int): Boolean? {
    if (database !== null) {
        val sql =
            "SELECT * from level WHERE guildId = $guildId AND userId = $userId"
        val user = database.prepareStatement(sql).executeQuery()
        if (user.next()) {
            val newxp = user.getInt("xp") + xp
            val newLevel = floor(0.1 * sqrt(newxp.toDouble())).toInt()
            val update_sql = "UPDATE level SET level = $newLevel, xp = $newxp WHERE guildId = $guildId AND userId = $userId"
            database.prepareStatement(update_sql).executeUpdate()
            return floor(0.1 * sqrt(user.getInt("xp").toDouble())) < newLevel
        } else {
            val insert_sql =
                "INSERT INTO level(userId, guildId, xp, level) VALUES ($userId, $guildId, $xp, ${
                    floor(
                        0.1 * sqrt(xp.toDouble())
                    ).toInt()
                })"
            database.prepareStatement(insert_sql).executeUpdate()
            return floor(0.1 * sqrt(xp.toDouble())) > 0
        }
    }
    return null
}

fun fetch(userId: Long, guildId: Long): ResultSet? {
    val sql =
        "SELECT * from level WHERE guildId = $guildId AND userId = $userId"
    val user = database?.prepareStatement(sql)?.executeQuery()
    return user
}