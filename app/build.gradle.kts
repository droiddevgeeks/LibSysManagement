plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("de.mannodermaus.android-junit5")
    id("dagger.hilt.android.plugin")
}

android {
    compileSdkVersion(Versions.sdkCompileVersion)
    buildToolsVersion(Versions.buildToolVersion)

    defaultConfig {
        applicationId = "com.example.libsysmanagement"
        minSdkVersion(Versions.sdkMinVersion)
        targetSdkVersion(Versions.sdkTargetVersion)
        versionCode = Versions.versionCode
        versionName = Versions.versionName
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    // For Kotlin projects
    kotlinOptions {
        jvmTarget = Versions.jvmTargetVersion
    }

    buildFeatures {
        viewBinding = true
    }

    sourceSets {
        getByName("main").java.srcDir("src/main/kotlin")
        getByName("test").java.srcDir("src/test/kotlin")
        getByName("androidTest").java.srcDir("src/androidTest/kotlin")
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

    implementation(deps.other.gson)
    //testing
    testImplementation(deps.testing.coreTesting)
    androidTestImplementation(deps.testing.androidxCoreTesting)
    testImplementation(deps.testing.mockitoInline)
    testImplementation(deps.testing.mockitoKotlin2)
    testImplementation(deps.testing.junitJupiterApi)
    testRuntimeOnly(deps.testing.junitJupiterEngine)
}
