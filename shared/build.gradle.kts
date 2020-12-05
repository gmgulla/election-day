import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("multiplatform")
    id("com.squareup.sqldelight")
    //kotlin("jvm") version "1.4.20"
}

group = "it.gmgulla"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val sqlDelightVersion: String by project
val kotlinJUnitRunnerVersion = "3.1.9"
val kotlinxDatetimeVersion: String by project

kotlin {
    jvm()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))
                implementation(kotlin("stdlib-jdk8"))
                implementation("com.squareup.sqldelight:runtime:$sqlDelightVersion")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation("com.squareup.sqldelight:sqlite-driver:$sqlDelightVersion")
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
            }
        }
    }
}

sqldelight {
    database("AppDatabase") {
        packageName = "it.gmgulla.electionDay.shared.db"
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

