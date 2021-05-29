package com.hands8142.discord

import com.hands8142.discord.Util.Database
import dev.kord.common.kColor
import dev.kord.gateway.Intents
import dev.kord.gateway.PrivilegedIntent
import me.jakejmattson.discordkt.api.dsl.bot
import dev.kord.x.emoji.Emojis
import io.github.cdimascio.dotenv.dotenv
import kotlinx.coroutines.flow.toList
import me.jakejmattson.discordkt.api.extensions.addField
import me.jakejmattson.discordkt.api.extensions.profileLink
import java.awt.Color

val dotenv = dotenv()
// val database = Database.connection()
val database = Database.createTable()
private val token = dotenv["TOKEN"]
private val prefix = dotenv["PREFIX"]

@OptIn(PrivilegedIntent::class, dev.kord.common.annotation.KordPreview::class)
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
            intents = Intents.all.values
        }

        localization {
            helpName = "도움말"
            helpDescription = "도움말 메뉴 표시"
            helpEmbedDescription = "자세한 내용은 `${prefix}${helpName} <command>`을 참조하십시오."
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

        permissions {
            true
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