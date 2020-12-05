group = "it.gmgulla"
version = "1.0-SNAPSHOT"


buildscript {
    repositories {
        mavenCentral()
        jcenter()
        maven {
            setUrl("https://plugins.gradle.org/m2/")
        }
    }
    val kotlinVersion: String by project
    val openjfxPluginVersion: String by project
    val sqlDelightVersion: String by project
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
        classpath("org.openjfx:javafx-plugin:$openjfxPluginVersion")
        classpath("com.squareup.sqldelight:gradle-plugin:$sqlDelightVersion")
    }
}

