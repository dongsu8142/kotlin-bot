package com.hands8142.discord.commands

import com.hands8142.discord.Util.checkPermission
import dev.kord.common.entity.Permission
import dev.kord.common.entity.Snowflake
import dev.kord.core.behavior.ban
import me.jakejmattson.discordkt.api.arguments.EveryArg
import me.jakejmattson.discordkt.api.arguments.UserArg
import me.jakejmattson.discordkt.api.dsl.commands

fun moderationCommands() = commands("Moderation") {
    guildCommand("킥") {
        description = "사용자를 추방합니다."
        execute(UserArg, EveryArg("사유").optionalNullable()) {
            val permission = checkPermission(author, discord, guild, Permission.KickMembers, user = true, bot = true)
            if (!permission) {
                respond("권한이 없습니다.")
                return@execute
            }
            guild.kick(args.first.id, args.second)
            respond(args.first.username + "을(를) 추방하였습니다.\n사유: " + args.second)
        }
    }

    guildCommand("밴") {
        description = "사용자를 밴합니다."
        execute(UserArg, EveryArg("사유").optionalNullable()) {
            val permission = checkPermission(author, discord, guild, Permission.KickMembers, user = true, bot = true)
            if (!permission) {
                respond("권한이 없습니다.")
                return@execute
            }
            guild.ban(args.first.id) {
                reason = args.second
            }
            respond(args.first.username + "을(를) 밴하였습니다.\n사유: " + args.second)
        }
    }

    guildCommand("언밴") {
        description = "사용자의 밴을 풀어줍니다."
        execute(EveryArg("id")) {
            val permission = checkPermission(author, discord, guild, Permission.KickMembers, user = true, bot = true)
            if (!permission) {
                respond("권한이 없습니다.")
                return@execute
            }
            guild.unban(Snowflake(args.first))
            respond(discord.kord.getUser(Snowflake(args.first))?.username + "의 밴을 풀었습니다.")
        }
    }
}
