package com.hands8142.discord.commands

import dev.kord.common.entity.Permission
import dev.kord.common.entity.Snowflake
import dev.kord.core.behavior.ban
import me.jakejmattson.discordkt.api.arguments.EveryArg
import me.jakejmattson.discordkt.api.arguments.UserArg
import me.jakejmattson.discordkt.api.dsl.commands

fun moderationCommands() = commands("Moderation") {
    guildCommand("킥") {
        description = "사용자를 추방합니다."
        execute(UserArg, EveryArg) {
            if(!author.asMember(guild.id).getPermissions().contains(Permission.KickMembers)) {
                respond("당신은 추방권한이 없습니다.")
                return@execute
            }
            if(!discord.kord.getSelf().asMember(guild.id).getPermissions().contains(Permission.KickMembers)) {
                respond("봇이 추방권한이 없습니다.")
                return@execute
            }
            guild.kick(args.first.id, args.second)
            respond(args.first.username + "을(를) 추방하였습니다.\n사유: " + args.second)
        }
    }

    guildCommand("밴") {
        description = "사용자를 밴합니다."
        execute(UserArg, EveryArg) {
            if(!author.asMember(guild.id).getPermissions().contains(Permission.BanMembers)) {
                respond("당신은 밴권한이 없습니다.")
                return@execute
            }
            if(!discord.kord.getSelf().asMember(guild.id).getPermissions().contains(Permission.BanMembers)) {
                respond("봇이 밴권한이 없습니다.")
                return@execute
            }
            guild.ban(args.first.id) {
                reason = args.second
                deleteMessagesDays = 1
            }
            respond(args.first.username + "을(를) 밴하였습니다.\n사유: " + args.second)
        }
    }

    guildCommand("언밴") {
        description = "사용자의 밴을 풀어줍니다."
        execute(EveryArg) {
            if(!author.asMember(guild.id).getPermissions().contains(Permission.BanMembers)) {
                respond("당신은 밴을 풀 수 있는 권한이 없습니다.")
                return@execute
            }
            if(!discord.kord.getSelf().asMember(guild.id).getPermissions().contains(Permission.BanMembers)) {
                respond("봇이 밴을 풀 수 있는 권한이 없습니다.")
                return@execute
            }
            guild.unban(Snowflake(args.first))
            respond(discord.kord.getUser(Snowflake(args.first))?.mention + "의 밴을 풀었습니다.")
        }
    }
}
