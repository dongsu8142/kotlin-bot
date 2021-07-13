package com.hands8142.discord.Util

import com.hands8142.discord.interfaces.API
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun request(): API {
    val retrofit = Retrofit.Builder()
        .baseUrl("https://koreaapi.herokuapp.com")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    return retrofit.create(API::class.java)
}