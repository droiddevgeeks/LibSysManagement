object Versions {
    const val kotlinVersion = "1.4.32"
    const val jvmTargetVersion = "1.8"
    const val versionName = "1.0.0"
    const val versionCode = 1
    const val sdkMinVersion = 16
    const val sdkTargetVersion = 30
    const val sdkCompileVersion = 30
    const val buildToolVersion = "30.0.3"

    const val gradleBuildToolVersion = "4.0.2"

    //SDK Version
    const val materialDesignVersion = "1.3.0"
    const val appcompatVersion = "1.2.0"
    const val coreKtxVersion = "1.3.2"
    const val ktxViewModelVersion = "2.3.1"
    const val androidxStableVersion = "1.2.0"
    const val constraintLayoutVersion = "2.1.0-beta01"
    const val retrofitVersion = "2.9.0"
    const val okHttpVersion = "4.9.0"

    const val rxJavaVersion = "2.2.20"
    const val rxAndroidVersion = "2.1.1"
    const val gsonVersion = "2.8.6"
    const val hiltVersion = "2.33-beta"

    const val archCoreTesting = "1.1.1"
    const val androidXCoreTesting = "2.1.0"
    const val mockitoKotlin = "2.2.0"
    const val junit5Version = "5.7.1"
    const val mockitoVersion = "3.9.0"
    const val gradlePluginJunitVersion = "1.5.2.0"
    const val qrScannerVersion = "3.4.0"
    const val multiDexVersion = "2.0.1"

}

object deps {
    object androidX {
        const val appCompat = "androidx.appcompat:appcompat:${Versions.appcompatVersion}"
        const val androidMaterial = "com.google.android.material:material:${Versions.materialDesignVersion}"
        const val constraintLayout = "androidx.constraintlayout:constraintlayout:${Versions.constraintLayoutVersion}"
        const val coreKtx = "androidx.core:core-ktx:${Versions.coreKtxVersion}"
        const val ktxFragment = "androidx.fragment:fragment-ktx:${Versions.coreKtxVersion}"
        const val recyclerView = "androidx.recyclerview:recyclerview:${Versions.androidxStableVersion}"
        const val viewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.ktxViewModelVersion}"
    }

    object kotlin {
        const val kotlinStdlibJdk = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlinVersion}"
    }

    object rx {
        const val java = "io.reactivex.rxjava2:rxjava:${Versions.rxJavaVersion}"
        const val android = "io.reactivex.rxjava2:rxandroid:${Versions.rxAndroidVersion}"
    }


    object square {
        const val okHttp = "com.squareup.okhttp3:okhttp:${Versions.okHttpVersion}"
        const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofitVersion}"
        const val retrofitGson = "com.squareup.retrofit2:converter-gson:${Versions.retrofitVersion}"
        const val retrofitRxAdapter = "com.squareup.retrofit2:adapter-rxjava2:${Versions.retrofitVersion}"
    }

    object di{
        const val hiltAndroid ="com.google.dagger:hilt-android:${Versions.hiltVersion}"
        const val hiltCompiler ="com.google.dagger:hilt-compiler:${Versions.hiltVersion}"
    }


    object testing {
        const val coreTesting = "android.arch.core:core-testing:${Versions.archCoreTesting}"
        const val androidxCoreTesting = "androidx.arch.core:core-testing:${Versions.androidXCoreTesting}"
        const val mockitoInline = "org.mockito:mockito-inline:${Versions.mockitoVersion}"
        const val junitJupiterApi = "org.junit.jupiter:junit-jupiter-api:${Versions.junit5Version}"
        const val junitJupiterEngine = "org.junit.jupiter:junit-jupiter-engine:${Versions.junit5Version}"
        const val mockitoKotlin2 = "com.nhaarman.mockitokotlin2:mockito-kotlin:${Versions.mockitoKotlin}"
    }

    object other {
        const val gson = "com.google.code.gson:gson:${Versions.gsonVersion}"
        const val qrScanner = "com.journeyapps:zxing-android-embedded:${Versions.qrScannerVersion}"
        const val multiDex = "androidx.multidex:multidex:${Versions.multiDexVersion}"
    }
}