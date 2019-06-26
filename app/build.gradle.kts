
import configs.AndroidConfig
import configs.KotlinConfig
import configs.ProguardConfig
import dependencies.InstrumentationTestsDependencies.Companion.instrumentationTest
import dependencies.UnitTestDependencies.Companion.unitTest
import modules.ModuleNames
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id(BuildPlugins.Ids.androidApplication)
    id(BuildPlugins.Ids.kotlinAndroid)
    id(BuildPlugins.Ids.kotlinAndroidExtensions)
}

repositories {
    google()
    maven(url = "https://jitpack.io")
}

base.archivesBaseName = "norris-${Versioning.version.name}"

android {
    compileSdkVersion(AndroidConfig.compileSdk)
    buildToolsVersion(AndroidConfig.buildToolsVersion)

    defaultConfig {

        minSdkVersion(AndroidConfig.minSdk)
        targetSdkVersion(AndroidConfig.targetSdk)

        applicationId = AndroidConfig.applicationId
        testInstrumentationRunner = AndroidConfig.instrumentationTestRunner
        versionCode = Versioning.version.code
        versionName = Versioning.version.name

        vectorDrawables.apply {
            useSupportLibrary = true
            generatedDensities(*(AndroidConfig.noGeneratedDensities))
        }

        resConfig("en")
    }

    buildTypes {

        getByName("debug") {
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-DEBUG"
            isTestCoverageEnabled = true
        }

        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true

            val proguardConfig = ProguardConfig("$rootDir/proguard")
            proguardFiles(*(proguardConfig.customRules))
            proguardFiles(getDefaultProguardFile(proguardConfig.androidRules))
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions.jvmTarget = KotlinConfig.targetJVM
    }

    testOptions {
        unitTests.isReturnDefaultValues = true
        unitTests.isIncludeAndroidResources = true
    }
}

dependencies {

    implementation(Libraries.kotlinStdlib)
    implementation(Libraries.appCompat)
    implementation(Libraries.coreAndroidx)
    implementation(Libraries.lifecycleViewModel)
    implementation(Libraries.lifecycleExtensions)
    implementation(Libraries.kodein)
    implementation(Libraries.kodeinConf)
    implementation(project(ModuleNames.Logger))
    implementation(project(ModuleNames.Domain))
    implementation(project(ModuleNames.Infrastructure.Rest))
    implementation(project(ModuleNames.Infrastructure.Networking))
    implementation(project(ModuleNames.Features.SharedAssets))
    implementation(project(ModuleNames.Features.Facts))

    unitTest {
        forEachDependency { testImplementation(it) }
    }

    instrumentationTest {
        forEachDependency { androidTestImplementation(it) }
    }

}

androidExtensions {
    extensions.add("experimental", true)
}