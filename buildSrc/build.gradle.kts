import org.gradle.kotlin.dsl.`kotlin-dsl`

plugins {
    `kotlin-dsl`
}

repositories {
    jcenter()
    mavenCentral()
    google()
}

dependencies {
    implementation ("com.android.tools.build:gradle:4.0.2")
    implementation ("org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.32")
}