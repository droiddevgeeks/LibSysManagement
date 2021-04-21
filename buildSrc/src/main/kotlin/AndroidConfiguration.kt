import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType

import com.android.build.gradle.BaseExtension
import org.gradle.api.JavaVersion

class AndroidConfiguration : Plugin<Project> {

    override fun apply(target: Project) {
        target.extensions.getByType<BaseExtension>().run {

            compileSdkVersion(Versions.sdkCompileVersion)
            buildToolsVersion(Versions.buildToolVersion)

            defaultConfig {
                minSdkVersion(Versions.sdkMinVersion)
                targetSdkVersion(Versions.sdkTargetVersion)
                versionCode = Versions.versionCode
                versionName = Versions.versionName
                testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                multiDexEnabled = true
            }

            compileOptions {
                sourceCompatibility = JavaVersion.VERSION_1_8
                targetCompatibility = JavaVersion.VERSION_1_8
            }

        }
    }
}