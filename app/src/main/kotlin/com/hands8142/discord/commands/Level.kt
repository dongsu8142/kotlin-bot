package com.hands8142.discord.commands

import com.hands8142.discord.Util.fetchLeaderboard
import com.hands8142.discord.Util.computeLeaderboard
import com.hands8142.discord.Util.fetch
import com.hands8142.discord.Util.xpFor
import dev.kord.common.kColor
import dev.kord.core.entity.User
import me.jakejmattson.discordkt.api.arguments.UserArg
import me.jakejmattson.discordkt.api.dsl.commands
import me.jakejmattson.discordkt.api.extensions.addInlineField

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
    guildCommand("랭크") {
        description = "랭크를 보여줍니다."
        execute(UserArg.optionalNullable()) {
            val target: User
            if (args.first !== null) {
                target = args.first!!
            } else {
                target = author
            }
            val user = fetch(target.id.value, guild.id.value)
            if (user !== null) {
                if (user.next()) {
                    val neededXp = xpFor(user.getLong("level") + 1)
                    val rawLeaderboard = fetchLeaderboard(
                        guild.id.value,
                        999999
                    )
                    if (rawLeaderboard !== null) {
                        val leaderboard = computeLeaderboard(
                            discord.kord,
                            rawLeaderboard
                        )
                        val rank = leaderboard.find { it["userId"] == target.id.value }
                        if (rank !== null) {
                            respond {
                                title = "Rank"
                                color = discord.configuration.theme?.kColor
                                addInlineField("경험치", user.getLong("xp").toString())
                                addInlineField("레벨", user.getLong("level").toString())
                                addInlineField("필요한 경험치", neededXp.toString())
                                addInlineField("랭크", rank["position"].toString())
                            }
                        } else {
                            respond("랭크를 찾을 수 없습니다.")
                        }
                    } else {
                        respond("데이터베이스가 꺼져있습니다.")
                    }
                }
            } else {
                respond("데이터베이스가 꺼져있습니다.")
            }
        }
    }
}