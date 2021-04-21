// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath ("com.android.tools.build:gradle:${Versions.gradleBuildToolVersion}")
        classpath ("org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlinVersion}")
        classpath ("de.mannodermaus.gradle.plugins:android-junit5:${Versions.gradlePluginJunitVersion}")
        classpath ("com.google.dagger:hilt-android-gradle-plugin:${Versions.hiltVersion}")
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

allprojects {
    repositories {
        google()
        jcenter()
    }
}

val clean by tasks.creating(Delete::class) {
    delete(rootProject.buildDir)
}