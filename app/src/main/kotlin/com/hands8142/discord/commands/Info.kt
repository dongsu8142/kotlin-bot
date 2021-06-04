package com.hands8142.discord.commands

import com.hands8142.discord.Util.timeFormat
import dev.kord.common.entity.ActivityType
import dev.kord.common.entity.ChannelType
import dev.kord.common.kColor
import dev.kord.rest.Image
import kotlinx.coroutines.flow.toList
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import me.jakejmattson.discordkt.api.arguments.UserArg
import me.jakejmattson.discordkt.api.dsl.commands
import me.jakejmattson.discordkt.api.extensions.addField
import me.jakejmattson.discordkt.api.extensions.addInlineField
import me.jakejmattson.discordkt.api.extensions.toTimeString
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
            val user = discord.kord.guilds.toList().sumOf { it.memberCount!! }
            val channel = discord.kord.guilds.toList().sumOf { it.channels.toList().count() }
            val createTime = me.id.timeStamp.toLocalDateTime(TimeZone.currentSystemDefault())
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
                            "**❯ DiscordKt:** ${discord.versions.library}" +
                            "**❯ Kord:** ${discord.versions.kord}\n"
                )
            }
        }
    }

    guildCommand("서버정보") {
        description = "서버정보를 보여줍니다."
        execute {
            val createTime = guild.id.timeStamp.toLocalDateTime(TimeZone.currentSystemDefault())
            val iconUrl = if (guild.getIconUrl(Image.Format.JPEG) !== null) {
                guild.getIconUrl(Image.Format.JPEG).toString()
            } else {
                ""
            }
            val roles = guild.roles.toList().map { it.mention }
            val emoji = guild.emojis.toList()
            val members = guild.members.toList()
            val channels = guild.channels.toList()
            val presence = members.mapNotNull { it.getPresenceOrNull() }
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
                            "**❯ 생성일:** ${timeFormat(createTime)}\n"
                )
                addField("통계",
                    "**❯ 역할 수:** ${roles.size}\n" +
                            "**❯ 이모티콘 수:** ${emoji.size}\n" +
                            "**❯ 일반 이모티콘 수:** ${emoji.filter { !it.isAnimated }.size}\n" +
                            "**❯ 애니메이션 이모티콘 수:** ${emoji.filter { it.isAnimated }.size}\n" +
                            "**❯ 회원 수:** ${guild.memberCount}\n" +
                            "**❯ 사람:** ${members.filter { !it.isBot }.size}\n" +
                            "**❯ 봇:** ${members.filter { it.isBot }.size}\n" +
                            "**❯ 채널 수:** ${channels.size}\n" +
                            "**❯ 텍스트 채널:** ${channels.filter { it.type == ChannelType.GuildText }.size}\n" +
                            "**❯ 음성 채널:** ${channels.filter { it.type == ChannelType.GuildVoice }.size}\n" +
                            "**❯ 부스트 수:** ${guild.premiumSubscriptionCount}"
                )
                addField("존재",
                    "**❯ 온라인:** ${presence.map { it.status.value }.filter { it == "online" }.size}\n" +
                            "**❯ 자리 비움:** ${presence.map { it.status.value }.filter { it == "idle" }.size}\n" +
                            "**❯ 다은 용무 중:** ${presence.map { it.status.value }.filter { it == "dnd" }.size}\n" +
                            "**❯ 오프라인:** ${presence.map { it.status.value }.filter { it == "offline" }.size + members.map{ it.getPresenceOrNull() }.filter { it == null }.size}\n"
                )
                addField("역할[${roles.size}]", roles.joinToString(", "))
            }
        }
    }

    guildCommand("유저정보") {
        description = "유저정보를 보여줍니다."
        execute(UserArg.optionalNullable()) {
            val game: String
            val status: String
            val target = if (args.first !== null) {
                args.first!!
            } else {
                author
            }
            val member = target.asMember(guild.id)
            val joinTime = member.joinedAt.toLocalDateTime(TimeZone.currentSystemDefault())
            val createTime = target.id.timeStamp.toLocalDateTime(TimeZone.currentSystemDefault())
            val roles = member.roles.toList().map { it.mention }
            if (member.getPresenceOrNull() !== null) {
                game = if (member.getPresence().activities.any { it.type == ActivityType.Game }) {
                    member.getPresence().activities.filter { it.type == ActivityType.Game }[0].name
                } else {
                    "게임 중이지 않습니다."
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

