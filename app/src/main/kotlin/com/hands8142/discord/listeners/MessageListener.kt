package com.hands8142.discord.listeners

import dev.kord.core.event.message.MessageCreateEvent
import me.jakejmattson.discordkt.api.dsl.listeners

fun MessageListener() = listeners {
    on<MessageCreateEvent> {
        if (message.author?.isBot == true)
            return@on
    }
}