package com.hands8142.discord

import com.hands8142.discord.Util.Config
import dev.kord.common.kColor
import dev.kord.gateway.Intent
import dev.kord.gateway.Intents
import me.jakejmattson.discordkt.api.dsl.bot
import dev.kord.x.emoji.Emojis
import kotlinx.coroutines.flow.toList
import me.jakejmattson.discordkt.api.extensions.addField
import me.jakejmattson.discordkt.api.extensions.profileLink
import me.jakejmattson.discordkt.api.locale.Language
import java.awt.Color

private val configBot = Config.getConfig().bot
private val token = configBot.token
private val prefix = configBot.prefix

@OptIn(dev.kord.common.annotation.KordPreview::class)
suspend fun main() {
    require(token != null) { "Expected the bot token as a command line argument!" }

    bot(token) {
        prefix {
            prefix
        }

        configure {
            allowMentionPrefix = true
            generateCommandDocs = true
            showStartupLog = true
            recommendCommands = true
            enableSearch = true
            commandReaction = Emojis.eyes
            theme = Color(0x00BFFF)
            intents = Intents(Intent.values).values
        }

        localeOf(Language.EN) {
            helpName = "도움말"
            helpDescription = "도움말 메뉴 표시"
            helpEmbedDescription = "자세한 내용은 `${prefix}${helpName} <command>`을 참조하십시오."
            unknownCommand = "알 수 없는 명령어"
            notFound = "찾을 수 없음"
            invalidFormat = "잘못된 형식"
            commandRecommendation = "추천 명령어: {0}"
            badArgs = "이 인수로 '{0}'를 실행할 수 없습니다."
        }

        mentionEmbed {
            title = "안녕하세요"
            color = it.discord.configuration.theme?.kColor

            author {
                with(it.author) {
                    icon = avatar.url
                    name = tag
                    url = profileLink
                }
            }

            thumbnail {
                url = it.discord.kord.getSelf().avatar.url
            }

            footer {
                val versions = it.discord.versions
                text = "${versions.library} - ${versions.kord} - ${versions.kotlin}"
            }

            addField("Prefix", it.prefix())
        }

        presence {
            playing("=도움말")
        }

        onStart {
            val guilds = kord.guilds.toList().joinToString { it.name }
            val guildCount = kord.guilds.toList().count()
            println("Guilds: $guilds")
            println("Guild Count: $guildCount")
            println("start")
        }
    }
}