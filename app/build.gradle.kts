apply<AndroidConfiguration>()

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("de.mannodermaus.android-junit5")
    id("dagger.hilt.android.plugin")
}

android {
    defaultConfig{
        applicationId = "com.example.libsysmanagement"
    }

    // For Kotlin projects
    kotlinOptions {
        jvmTarget = Versions.jvmTargetVersion
    }

    buildFeatures {
        viewBinding = true
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(deps.kotlin.kotlinStdlibJdk)
    implementation(deps.androidX.appCompat)
    implementation(deps.androidX.androidMaterial)
    implementation(deps.androidX.constraintLayout)
    implementation(deps.androidX.coreKtx)
    implementation(deps.androidX.ktxFragment)
    implementation(deps.androidX.recyclerView)
    implementation(deps.androidX.viewModel)


    implementation(project(":corenetworking"))

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
    implementation(deps.other.qrScanner)
    implementation(deps.other.multiDex)

    //testing
    testImplementation(deps.testing.coreTesting)
    androidTestImplementation(deps.testing.androidxCoreTesting)
    testImplementation(deps.testing.mockitoInline)
    testImplementation(deps.testing.mockitoKotlin2)
    testImplementation(deps.testing.junitJupiterApi)
    testRuntimeOnly(deps.testing.junitJupiterEngine)
}
