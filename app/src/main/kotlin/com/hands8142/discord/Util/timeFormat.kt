package com.hands8142.discord.Util

import java.time.ZonedDateTime

fun timeFormat(time: ZonedDateTime): String {
    return "${time.year}년 ${time.monthValue}월 ${time.dayOfMonth}일"
}