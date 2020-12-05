plugins {
    java
    kotlin("jvm")
    id("org.openjfx.javafxplugin")
    application
}

group = "it.gmgulla"
version = "1.0-SNAPSHOT"

val tornadofxVersion = "1.7.20"
val requiredKotlinReflectVersion = "1.4.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":shared"))
    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.kotlin:kotlin-reflect:$requiredKotlinReflectVersion")
    implementation("no.tornado:tornadofx:$tornadofxVersion")
    implementation("junit", "junit", "4.12")
}

javafx {
    version = "11.0.2"
    modules("javafx.controls", "javafx.graphics")
}

application {
    mainClassName = "it.gmgulla.electionDay.jvmApp.MainKt"
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().all {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

