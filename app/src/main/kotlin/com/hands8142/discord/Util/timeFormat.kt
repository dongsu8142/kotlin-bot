package com.hands8142.discord.Util

import kotlinx.datetime.LocalDateTime

fun timeFormat(time: LocalDateTime): String {
    return "${time.year}. ${time.monthNumber}. ${time.dayOfMonth}. ${time.hour}:${time.minute}:${time.second}"
}
