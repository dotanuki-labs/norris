import configs.AndroidConfig
import configs.KotlinConfig
import configs.ProguardConfig
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id(BuildPlugins.Ids.androidApplication)
    id(BuildPlugins.Ids.kotlinAndroid)
    id("com.slack.keeper") version "0.10.0"
}

repositories {
    google()
}

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
        kotlinOptions.freeCompilerArgs += listOf(
            "-Xopt-in=kotlin.time.ExperimentalTime",
            "-Xopt-in=kotlin.RequiresOptIn"
        )
    }

    testOptions {
        unitTests.isReturnDefaultValues = true
        unitTests.isIncludeAndroidResources = true
    }
}

dependencies {
    implementation(project(":platform:logger"))
    implementation(project(":platform:rest-chucknorris"))
    implementation(project(":platform:networking"))
    implementation(project(":platform:persistance"))
    implementation(project(":platform:shared-assets"))
    implementation(project(":platform:shared-utilities"))
    implementation(project(":platform:navigator"))
    implementation(project(":features:onboarding"))
    implementation(project(":features:facts"))
    implementation(project(":features:search"))

    implementation("androidx.appcompat:appcompat:1.3.0")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    implementation("androidx.core:core-ktx:1.6.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.3.1")
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation("org.kodein.di:kodein-di-jvm:7.6.0")
    implementation("com.squareup.okhttp3:okhttp:4.9.1")

    androidTestImplementation(project(":platform:testing-rest"))

    androidTestImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test:core:1.4.0")
    androidTestImplementation("androidx.test:core-ktx:1.4.0")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.ext:junit-ktx:1.1.3")
    androidTestImplementation("androidx.test:runner:1.4.0")
    androidTestImplementation("androidx.test:rules:1.4.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.3.0")
    androidTestImplementation("com.schibsted.spain:barista:3.9.0")
    androidTestImplementation("com.google.truth:truth:1.1.3")
    androidTestImplementation("com.squareup.okhttp3:mockwebserver:4.9.1")
}

fun Project.evaluateTestMode(): Boolean =
    properties["testMode"]?.let { true } ?: false

fun Project.evaluateAPIUrl(): String =
    properties["testMode"]?.let { "http://localhost:4242" } ?: "https://api.chucknorris.io"
