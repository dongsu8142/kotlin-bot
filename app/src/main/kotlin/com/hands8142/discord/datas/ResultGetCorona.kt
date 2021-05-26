package com.hands8142.discord.datas

data class ResultGetCorona (
    val success: Boolean,
    val message: String,
    val time: String,
    val 확진환자: String,
    val 완치환자: String,
    val 치료중: String,
    val 사망: String,
    val 누적확진률: String,
    val 치사율: String
)