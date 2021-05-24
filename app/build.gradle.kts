plugins {
    id("org.jetbrains.kotlin.jvm") version "1.4.32"
    application
}

repositories {
    mavenCentral()
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
}

dependencies {
    implementation("me.jakejmattson:DiscordKt:0.22.0-SNAPSHOT")
    implementation("io.github.cdimascio:dotenv-kotlin:6.2.2")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
}

application {
    mainClass.set("com.hands8142.discord.AppKt")
}

tasks.compileKotlin {
    kotlinOptions.jvmTarget = "1.8"
}
