package com.hands8142.discord.interfaces

import com.hands8142.discord.datas.*
import com.hands8142.discord.datas.unsplash.ResultGetUnsplash
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface API {
    @GET("/corona")
    fun getCorona(): Call<ResultGetCorona>

    @GET("/music")
    fun getMusic(): Call<ResultGetMusic>

    @GET("/search/photos")
    fun getUnsplash(
        @Query("client_id") AccessKey: String,
        @Query("query") query: String,
        @Query("per_page") maxImage: Int
    ): Call<ResultGetUnsplash>
}