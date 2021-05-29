package com.hands8142.discord.commands

import com.hands8142.discord.Util.fetchLeaderboard
import com.hands8142.discord.Util.computeLeaderboard
import me.jakejmattson.discordkt.api.dsl.commands

fun levelCommand() = commands("level") {
    guildCommand("리더보드") {
        description = "리더보드를 보여줍니다."
        execute {
            val rawLeaderboard = fetchLeaderboard(guild.id.value, 10)
            if (rawLeaderboard !== null) {
                if (rawLeaderboard.isNotEmpty()) {
                    val leaderboard = computeLeaderboard(discord.kord, rawLeaderboard)
                    val lb = leaderboard.map {
                        "${it["position"]}. ${it["username"]}#${it["discriminator"]}\nLevel: ${
                            it["level"]
                        }\nXP: ${it["xp"]}"
                    }
                    respond(lb.joinToString("\n"))
                } else {
                    respond("아직 리더보드 안에 아무도 없습니다.")
                }
            } else {
                respond("데이터베이스가 꺼져있습니다.")
            }
        }
    }
}