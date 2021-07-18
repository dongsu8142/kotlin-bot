package com.hands8142.discord.services

import me.jakejmattson.discordkt.api.dsl.PermissionContext
import me.jakejmattson.discordkt.api.dsl.PermissionSet

enum class Permissions : PermissionSet {
    EVERYONE {
        override suspend fun hasPermission(context: PermissionContext) = true
    }
}