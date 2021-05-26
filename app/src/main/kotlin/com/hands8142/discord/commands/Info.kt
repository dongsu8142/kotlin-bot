package com.hands8142.discord.commands

import com.hands8142.discord.Util.timeFormat
import dev.kord.common.entity.ActivityType
import dev.kord.common.kColor
import dev.kord.core.entity.User
import dev.kord.rest.Image
import kotlinx.coroutines.flow.toList
import me.jakejmattson.discordkt.api.arguments.UserArg
import me.jakejmattson.discordkt.api.dsl.commands
import me.jakejmattson.discordkt.api.extensions.addField
import me.jakejmattson.discordkt.api.extensions.addInlineField
import me.jakejmattson.discordkt.api.extensions.toTimeString
import java.time.ZoneId
import java.util.*

private val startTime = Date()

fun infoCommand() = commands("Info") {
    command("봇정보") {
        description = "봇의 정보를 보여줍니다."
        execute {
            val me = discord.kord.getSelf()
            val name = me.tag + " (" + me.id.value + ")"
            val uptime = ((Date().time - startTime.time) / 1000).toTimeString()
            val server = discord.kord.guilds.toList().count()
            val user = discord.kord.guilds.toList().sumBy { it.memberCount!! }
            val channel = discord.kord.guilds.toList().sumBy { it.channels.toList().count() }
            val createTime = me.id.timeStamp.atZone(ZoneId.systemDefault())
            respond {
                color = discord.configuration.theme?.kColor
                thumbnail {
                    url = me.avatar.url
                }
                addField("일반",
                    "**❯ 봇:** $name\n" +
                            "**❯ 업타임:** $uptime\n" +
                            "**❯ 서버:** $server\n" +
                            "**❯ 유저:** $user\n" +
                            "**❯ 채널:** $channel\n" +
                            "**❯ 만든날짜:** ${timeFormat(createTime)}\n" +
                            "**❯ Kotlin:** ${discord.versions.kotlin}\n" +
                            "**❯ DiscordKt:** ${discord.versions.library}"
                )
            }
        }
    }

    guildCommand("서버정보") {
        description = "서버정보를 보여줍니다."
        execute {
            val iconUrl: String
            val createTime = guild.id.timeStamp.atZone(ZoneId.systemDefault())
            if (guild.getIconUrl(Image.Format.JPEG) !== null) {
                iconUrl = guild.getIconUrl(Image.Format.JPEG).toString()
            } else {
                iconUrl = ""
            }
            respond {
                description = "${guild.name}의 정보"
                color = discord.configuration.theme?.kColor
                thumbnail {
                    url = iconUrl
                }
                addField("일반",
                    "**❯ 이름:** ${guild.name}\n" +
                            "**❯ 아이디:** ${guild.id.value}\n" +
                            "**❯ 소유자:** ${guild.owner.asMember().tag}\n" +
                            "**❯ 위치:** ${guild.getRegion().name}\n" +
                            "**❯ 부스트 티어:** ${guild.premiumTier.value}\n" +
                            "**❯ 보안 레벨:** ${guild.verificationLevel.value}\n" +
                            "**❯ 생성일:** ${timeFormat(createTime)}"
                )
            }
        }
    }

    guildCommand("유저정보") {
        description = "유저정보를 보여줍니다."
        execute(UserArg.optionalNullable()) {
            val target: User
            val game: String
            val status: String
            if (args.first !== null) {
                target = args.first!!
            } else {
                target = author
            }
            val member = target.asMember(guild.id)
            val joinTime = member.joinedAt.atZone(ZoneId.systemDefault())
            val createTime = target.id.timeStamp.atZone(ZoneId.systemDefault())
            val roles = member.roles.toList().map { it.mention }
            if (member.getPresenceOrNull() !== null) {
                if (member.getPresence().activities.filter { it.type == ActivityType.Game }.isNotEmpty()) {
                    game = member.getPresence().activities.filter { it.type == ActivityType.Game }[0].name
                } else {
                    game = "게임 중이지 않습니다."
                }
                status = member.getPresence().status.value
            } else {
                status = "offline"
                game = "게임 중이지 않습니다."
            }
            respond {
                title = target.username + nickname(member.nickname)
                color = discord.configuration.theme?.kColor
                author {
                    name = target.username
                    icon = target.avatar.url
                }
                description = "**이름**: ${target.username}\n**태그**: ${target.username}#${target.discriminator}\n**아이디**: ${target.id.value}"
                addInlineField("상태", status)
                addInlineField("게임", game)
                addInlineField("서버 참여 시간", timeFormat(joinTime))
                addInlineField("계정 생성 일", timeFormat(createTime))
                addInlineField("역할", "**[${roles.size}]:** " + roles.joinToString(", "))
            }
        }
    }
}

private fun nickname(name: String?): String {
    if (name == null) {
        return ""
    }
    return "($name)"
}

