package com.hands8142.discord.interfaces

import com.hands8142.discord.datas.ResultGetCorona
import com.hands8142.discord.datas.ResultGetMusic
import retrofit2.Call
import retrofit2.http.GET

interface API {
    @GET("/corona")
    fun getCorona(): Call<ResultGetCorona>

    @GET("/music")
    fun getMusic(): Call<ResultGetMusic>
}