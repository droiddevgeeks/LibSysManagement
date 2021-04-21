apply<AndroidConfiguration>()

plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("de.mannodermaus.android-junit5")
    id("dagger.hilt.android.plugin")
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(deps.kotlin.kotlinStdlibJdk)


    /* retrofit for network calls */
    implementation(deps.square.retrofit)
    implementation(deps.square.retrofitGson)
    implementation(deps.square.retrofitRxAdapter)
    implementation(deps.square.okHttp)


    implementation(deps.rx.java)
    implementation(deps.rx.android)

    //hilt
    implementation(deps.di.hiltAndroid)
    kapt(deps.di.hiltCompiler)

    //other
    implementation(deps.other.gson)

    //testing
    testImplementation(deps.testing.mockitoInline)
    testImplementation(deps.testing.mockitoKotlin2)
    testImplementation(deps.testing.junitJupiterApi)
    testRuntimeOnly(deps.testing.junitJupiterEngine)
}