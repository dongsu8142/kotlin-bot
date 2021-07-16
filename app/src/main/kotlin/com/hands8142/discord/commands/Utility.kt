package com.hands8142.discord.commands

import com.google.gson.Gson
import com.hands8142.discord.Util.Config
import com.hands8142.discord.Util.request
import dev.kord.common.kColor
import me.jakejmattson.discordkt.api.arguments.EveryArg
import me.jakejmattson.discordkt.api.arguments.IntegerRangeArg
import me.jakejmattson.discordkt.api.dsl.commands
import me.jakejmattson.discordkt.api.extensions.addField
import me.jakejmattson.discordkt.api.extensions.addInlineField
import org.json.JSONObject
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

private val apiConfig = Config.getConfig().api
private val AccessKey = apiConfig.unsplash

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
            val response = request().getCorona().awaitResponse()
            if (response.isSuccessful) {
                val corona = response.body()!!
                if (corona.success) {
                    respond {
                        title = "Covid-19 Virus Korea Status"
                        color = discord.configuration.theme?.kColor
                        addField(
                            "Data source : Ministry of Health and Welfare of Korea",
                            "http://ncov.mohw.go.kr/index.jsp"
                        )
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
            } else {
                respond("코로나 정보를 불러오는데 실패했습니다.")
            }
        }
    }

    command("음악차트", "노래차트", "음악순위", "노래순위") {
        description = "멜론의 음악차트를 알려줍니다."
        execute(IntegerRangeArg(1, 100, "단일 또는 범위(1-100)").optionalNullable(), IntegerRangeArg(1, 100, "범위(1-100)").optionalNullable()) {
            val response = request().getMusic().awaitResponse()
            if (response.isSuccessful) {
                val music = response.body()!!
                val jsonString = Gson().toJson(music).trimIndent()
                val json = JSONObject(jsonString)
                val first = args.first
                val second = args.second
                if (music.success) {
                    when {
                        first == null -> {
                            respond {
                                title = "음악차트"
                                color = discord.configuration.theme?.kColor
                                for (i in 1..24) {
                                    addInlineField("${i}위", json.getString(i.toString()))
                                }
                            }
                        }
                        second == null -> {
                            respond(first.toString() + "위: " + json.getString(first.toString()))
                        }
                        first < second -> {
                            respond {
                                title = "${first}부터 ${second}까지 음악차트"
                                color = discord.configuration.theme?.kColor
                                for (i in first..second) {
                                    addInlineField("${i}위", json.getString(i.toString()))
                                }
                            }
                        }
                        else -> {
                            respond("범위를 다시 지정해 주세요.")
                        }
                    }
                } else {
                    respond(music.message)
                }
            } else {
                respond("음악차트를 가지고 오는데 실패하였습니다")
            }
        }
    }

    command("이미지", "사진"){
        description = "사진을 찾아줍니다."
        execute(EveryArg("사진 이름")) {
            val response = request().getUnsplash(AccessKey, args.first, 20).awaitResponse()
            if (response.isSuccessful){
                if (response.body()?.total != 0){
                    val random = Random().nextInt(response.body()?.results?.size!!)
                    respond(response.body()!!.results[random].urls.raw)
                } else {
                    respond("검색 결과가 없습니다.")
                }
            } else {
                respond("사진을 불러오는데 실패하였습니다.")
            }
        }
    }
}