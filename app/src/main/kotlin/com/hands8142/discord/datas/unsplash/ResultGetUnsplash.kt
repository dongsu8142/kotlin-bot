package com.hands8142.discord.datas.unsplash

data class ResultGetUnsplash(
    val results: List<Result>,
    val total: Int,
    val total_pages: Int
)
