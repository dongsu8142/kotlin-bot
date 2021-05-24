package com.hands8142.discord.interfaces

import com.hands8142.discord.datas.ResultGetCorona
import retrofit2.Call
import retrofit2.http.GET

interface CoronaAPI {
    @GET("/corona")
    fun getCorona(): Call<ResultGetCorona>
}