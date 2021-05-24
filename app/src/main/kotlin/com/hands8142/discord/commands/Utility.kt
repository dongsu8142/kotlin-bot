package com.hands8142.discord.commands

import com.hands8142.discord.interfaces.CoronaAPI
import dev.kord.common.kColor
import me.jakejmattson.discordkt.api.dsl.commands
import me.jakejmattson.discordkt.api.extensions.addField
import me.jakejmattson.discordkt.api.extensions.addInlineField
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

fun utilityCommands() = commands("Utility") {
    command("핑") {
        description = "레이턴시를 보여줍니다"
        execute {
            respond(discord.kord.gateway.averagePing.toString())
        }
    }

    command("코로나") {
        description = "현재 코로나 상황을 보여줍니다."
        execute {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://manyyapi.herokuapp.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            val api = retrofit.create(CoronaAPI::class.java)
            val response = api.getCorona().awaitResponse()
            val corona = response.body()!!
            if (corona.success) {
                respond {
                    title = "Covid-19 Virus Korea Status"
                    color = discord.configuration.theme?.kColor
                    addField("Data source : Ministry of Health and Welfare of Korea", "http://ncov.mohw.go.kr/index.jsp")
                    addField("Latest data refred time", "해당 자료는 ${corona.time} 자료입니다.")
                    addInlineField("확진환자(누적)", corona.확진환자)
                    addInlineField("완치환자(격리해제)", corona.완치환자)
                    addInlineField("치료중(격리 중)", corona.치료중)
                    addInlineField("사망", corona.사망)
                    addInlineField("누적확진률", corona.누적확진률)
                    addInlineField("치사율", corona.치사율)
                }
            } else {
                respond(corona.message)
            }
        }
    }
}