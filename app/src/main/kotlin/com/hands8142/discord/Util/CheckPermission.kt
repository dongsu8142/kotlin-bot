package com.hands8142.discord.Util

import dev.kord.common.entity.Permission
import dev.kord.core.entity.Guild
import dev.kord.core.entity.User
import me.jakejmattson.discordkt.api.Discord

suspend fun checkPermission(author: User, discord: Discord, guild: Guild, permission: Permission, user: Boolean, bot: Boolean): Boolean {
    if (user) {
        if (!author.asMember(guild.id).getPermissions().contains(permission)) {
            return false
        }
    }
    if (bot) {
        if (!discord.kord.getSelf().asMember(guild.id).getPermissions().contains(permission)) {
            return false
        }
    }
    return true
}
