import configs.AndroidConfig
import configs.KotlinConfig
import configs.ProguardConfig
import dependencies.InstrumentationTestsDependencies.Companion.instrumentationTest
import dependencies.UnitTestDependencies.Companion.unitTest
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id(BuildPlugins.Ids.androidApplication)
    id(BuildPlugins.Ids.kotlinAndroid)
    id(BuildPlugins.Ids.kotlinAndroidExtensions)
    id(BuildPlugins.Ids.keeper) version BuildPlugins.Versions.keeper
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

        testBuildType = "release"
    }

    signingConfigs {
        create("release") {
            storeFile = rootProject.file("signing/dotanuki-demos.jks")
            storePassword = "dotanuki"
            keyAlias = "dotanuki-alias"
            keyPassword = "dotanuki"
        }
    }

    buildTypes {

        getByName("debug") {
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-DEBUG"
            isTestCoverageEnabled = true
            buildConfigField("String", "CHUCKNORRIS_API_URL", "\"${project.evaluateAPIUrl()}\"")
            resValue("bool", "clear_networking_traffic_enabled", "${project.evaluateTestMode()}")
        }

        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true

            val proguardConfig = ProguardConfig("$rootDir/proguard")
            proguardFiles(*(proguardConfig.customRules))
            proguardFiles(getDefaultProguardFile(proguardConfig.androidRules))

            buildConfigField("String", "CHUCKNORRIS_API_URL", "\"${project.evaluateAPIUrl()}\"")
            resValue("bool", "clear_networking_traffic_enabled", "${project.evaluateTestMode()}")
            signingConfig = signingConfigs.findByName("release")
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
    implementation(Libraries.swipeToRefresh)
    implementation(Libraries.coreAndroidx)
    implementation(Libraries.lifecycleViewModel)
    implementation(Libraries.lifecycleExtensions)
    implementation(Libraries.kodein)
    implementation(Libraries.okhttp)
    implementation(project(":platform:logger"))
    implementation(project(":platform:domain"))
    implementation(project(":platform:rest-chucknorris"))
    implementation(project(":platform:networking"))
    implementation(project(":platform:persistance"))
    implementation(project(":platform:shared-assets"))
    implementation(project(":platform:shared-utilities"))
    implementation(project(":platform:navigator"))
    implementation(project(":features:onboarding"))
    implementation(project(":features:facts"))
    implementation(project(":features:search"))

    androidTestImplementation(Libraries.mockWebServer)

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

fun Project.evaluateTestMode(): Boolean =
    properties["testMode"]?.let { true } ?: false

fun Project.evaluateAPIUrl(): String =
    properties["testMode"]?.let { "http://localhost:4242" } ?: "https://api.chucknorris.io"