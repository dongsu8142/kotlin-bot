import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.5.21"
    id("com.github.johnrengelman.shadow") version "7.0.0"
    application
}

repositories {
    mavenCentral()
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("me.jakejmattson:DiscordKt:0.22.0-SNAPSHOT")
    implementation("org.yaml:snakeyaml:1.29")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.google.code.gson:gson:2.8.6")
    implementation("org.json:json:20210307")
    implementation("mysql:mysql-connector-java:8.0.25")
}

application {
    mainClass.set("com.hands8142.discord.AppKt")
}

tasks.compileKotlin {
    kotlinOptions.jvmTarget = "1.8"
}

val shadowJar: ShadowJar by tasks
shadowJar.apply {
    manifest.attributes.apply {
        put("Main-Class", application.mainClass)
    }
    archiveFileName.set("bot.jar")
}
