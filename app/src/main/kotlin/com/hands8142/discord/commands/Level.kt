package com.hands8142.discord.commands

import com.hands8142.discord.Util.LevelSystem
import com.hands8142.discord.Util.checkPermission
import dev.kord.common.entity.Permission
import dev.kord.common.kColor
import me.jakejmattson.discordkt.api.arguments.LongArg
import me.jakejmattson.discordkt.api.arguments.UserArg
import me.jakejmattson.discordkt.api.dsl.commands
import me.jakejmattson.discordkt.api.extensions.addInlineField

fun levelCommand() = commands("level") {
    guildCommand("리더보드") {
        description = "리더보드를 보여줍니다."
        execute {
            val rawLeaderboard = LevelSystem.fetchLeaderboard(guild.id.value, 10)
            if (rawLeaderboard !== null) {
                if (rawLeaderboard.isNotEmpty()) {
                    val leaderboard = LevelSystem.computeLeaderboard(discord.kord, rawLeaderboard)
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
            val target = if (args.first !== null) {
                args.first!!
            } else {
                author
            }
            val user = LevelSystem.fetch(target.id.value, guild.id.value)
            if (user !== null) {
                if (user.next()) {
                    val neededXp = LevelSystem.xpFor(user.getLong("level") + 1)
                    val rawLeaderboard = LevelSystem.fetchLeaderboard(
                        guild.id.value,
                        999999
                    )
                    if (rawLeaderboard !== null) {
                        val leaderboard = LevelSystem.computeLeaderboard(
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
                } else {
                    respond("유저를 찾을 수 없습니다.")
                }
            } else {
                respond("데이터베이스가 꺼져있습니다.")
            }
        }
    }
    guildCommand("경험치주기") {
        description = "경험치를 부여합니다."
        execute(UserArg, LongArg("경험치")) {
            val permission = checkPermission(author, discord, guild, Permission.Administrator, user = true, bot = false)
            if (!permission) {
                respond("권한이 없습니다.")
                return@execute
            }
            if (args.second <= 0) {
                respond("0이하는 불가능합니다.")
                return@execute
            }
            if (LevelSystem.checkDatabase()) {
                val hasLeveledUp = LevelSystem.appendXp(args.first.id.value, guild.id.value, args.second)
                respond("${args.first.mention}님의 경험치를 ${args.second}만큼 부여합니다.")
                if (hasLeveledUp) {
                    val user = LevelSystem.fetch(args.first.id.value, guild.id.value)!!
                    if (user.next()) {
                        val level = user.getLong("level")
                        message.channel.createMessage("축하합니다 ${args.first.mention}님, 레벨이 상승하였습니다 ${level}!!")
                    }
                }
            } else {
                respond("데이터베이스가 꺼져있습니다.")
            }
        }
    }
    guildCommand("경험치뺏기") {
        description = "경험치를 차감합니다."
        execute(UserArg, LongArg("경험치")) {
            val permission = checkPermission(author, discord, guild, Permission.Administrator, user = true, bot = false)
            if (!permission) {
                respond("권한이 없습니다.")
                return@execute
            }
            if (args.second <= 0) {
                respond("0이하는 불가능합니다.")
                return@execute
            }
            if (LevelSystem.checkDatabase()) {
                LevelSystem.appendXp(args.first.id.value, guild.id.value, -args.second)
                respond("${args.first.mention}님의 경험치를 ${args.second}만큼 차감합니다.")
            } else {
                respond("데이터베이스가 꺼져있습니다.")
            }
        }
    }
}