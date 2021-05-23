package com.hands8142.discord.commands

import me.jakejmattson.discordkt.api.dsl.commands

fun utilityCommands() = commands("Utility") {
    command("핑") {
        description = "레이턴시를 보여줍니다"
        execute {
            respond(discord.kord.gateway.averagePing.toString())
        }
    }
}