package com.hands8142.discord

import dev.kord.common.kColor
import dev.kord.gateway.Intents
import me.jakejmattson.discordkt.api.dsl.bot
import dev.kord.x.emoji.Emojis
import io.github.cdimascio.dotenv.dotenv
import kotlinx.coroutines.flow.toList
import me.jakejmattson.discordkt.api.extensions.addField
import me.jakejmattson.discordkt.api.extensions.profileLink
import java.awt.Color

suspend fun main() {
    val dotenv = dotenv()
    val token = dotenv["TOKEN"]
    require(token != null) { "Expected the bot token as a command line argument!" }

    bot(token) {
        prefix {
            "="
        }

        configure {
            allowMentionPrefix = true
            generateCommandDocs = true
            showStartupLog = true
            recommendCommands = true
            commandReaction = Emojis.eyes
            theme = Color(0x00BFFF)
            intents = Intents.nonPrivileged.values
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
            playing("discordkt")
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