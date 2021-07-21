package com.hands8142.discord.Util

import com.hands8142.discord.datas.config.ConfigDTO
import org.yaml.snakeyaml.Yaml
import java.io.File
import java.io.FileInputStream

private val yaml = Yaml().loadAs(FileInputStream(File("config.yml")), ConfigDTO::class.java)

object Config {
    fun getConfig(): ConfigDTO {
        return yaml
    }
}
