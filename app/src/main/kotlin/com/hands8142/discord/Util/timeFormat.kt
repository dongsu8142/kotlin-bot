package com.hands8142.discord.Util

import java.time.ZonedDateTime

fun timeFormat(time: ZonedDateTime): String {
    return "${time.year}. ${time.monthValue}. ${time.dayOfMonth}. ${time.hour}:${time.minute}:${time.second}"
}