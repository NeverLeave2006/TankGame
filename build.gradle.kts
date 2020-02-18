import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.50"
    application
}
application{
    mainClassName="top.snowlands.game.AppKt"
}
dependencies {
    implementation(kotlin("stdlib-jdk8"))
    compile("com.github.shaunxiao:kotlinGameEngine:v0.0.4")
}
repositories {
    mavenCentral()
    maven{setUrl("https://jitpack.io")}
}
val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
}
val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "1.8"
}

