package com.hands8142.discord.listeners

import com.hands8142.discord.Util.appendXp
import com.hands8142.discord.Util.fetch
import dev.kord.core.event.message.MessageCreateEvent
import me.jakejmattson.discordkt.api.dsl.listeners
import java.util.*

fun MessageListener() = listeners {
    on<MessageCreateEvent> {
        if (message.author?.isBot == true)
            return@on
        if (message.getGuildOrNull() !== null) {
            val randomXp = Random().nextInt(9) + 1
            val hasLeveledUp = appendXp(message.author?.id?.value!!, guildId!!.value, randomXp)
            if (hasLeveledUp) {
                val user = fetch(message.author?.id?.value!!, guildId!!.value)
                if (user.next()) {
                    val level = user.getInt("level")
                    message.channel.createMessage("축하합니다 ${message.author?.mention}님, 레벨이 상승하였습니다 ${level}!!")
                }
            }
        }
    }
}