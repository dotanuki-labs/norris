import com.android.build.api.dsl.BuildType
import definitions.AndroidDefinitions
import definitions.ProguardDefinitions
import definitions.Versioning
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.io.FileInputStream
import java.util.Properties

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("com.slack.keeper") version "0.11.0"
}

repositories {
    google()
}

android {
    compileSdk = AndroidDefinitions.compileSdk
    buildToolsVersion = AndroidDefinitions.buildToolsVersion

    defaultConfig {
        minSdk = AndroidDefinitions.minSdk
        targetSdk = AndroidDefinitions.targetSdk

        applicationId = AndroidDefinitions.applicationId
        testInstrumentationRunner = AndroidDefinitions.instrumentationTestRunner
        versionCode = Versioning.version.code
        versionName = Versioning.version.name

        vectorDrawables.apply {
            useSupportLibrary = true
            generatedDensities(*(AndroidDefinitions.noGeneratedDensities))
        }

        resourceConfigurations.add("en")
        testBuildType = evaluateTestBuildType()
    }

    signingConfigs {
        create("release") {
            loadSigningProperties().run {
                storeFile = File("$rootDir/dotanuki-demos.jks")
                storePassword = getProperty("io.dotanuki.norris.storepass")
                keyAlias = getProperty("io.dotanuki.norris.keyalias")
                keyPassword = getProperty("io.dotanuki.norris.keypass")
            }
        }
    }

    buildTypes {

        getByName("debug") {
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-DEBUG"
            isTestCoverageEnabled = true
            configureHttps()
        }

        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true

            val proguardConfig = ProguardDefinitions("$rootDir/app/proguard")
            proguardFiles(*(proguardConfig.customRules))
            proguardFiles(getDefaultProguardFile(proguardConfig.androidRules))
            signingConfig = signingConfigs.findByName("release")
            configureHttps()
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "11"
        kotlinOptions.freeCompilerArgs += listOf(
            "-Xopt-in=kotlin.time.ExperimentalTime",
            "-Xopt-in=kotlin.RequiresOptIn"
        )
    }

    testOptions {
        unitTests.isReturnDefaultValues = true
        unitTests.isIncludeAndroidResources = true
    }

    packagingOptions {
        jniLibs.useLegacyPackaging = true
    }
}

dependencies {
    implementation(project(":platform:core:rest-chucknorris"))
    implementation(project(":platform:core:persistance"))
    implementation(project(":platform:core:navigator"))
    implementation(project(":platform:common:common-kodein"))
    implementation(project(":platform:common:common-static"))
    implementation(project(":features:onboarding"))
    implementation(project(":features:facts"))
    implementation(project(":features:search"))

    implementation("androidx.core:core:1.6.0")
    implementation("androidx.core:core-ktx:1.6.0")
    implementation("androidx.appcompat:appcompat:1.3.1")
    implementation("org.kodein.di:kodein-di-jvm:7.7.0")
    implementation("org.kodein.type:kodein-type-jvm:1.7.1")
    implementation("com.squareup.okhttp3:okhttp:4.9.1")

    androidTestImplementation(project(":platform:testing:testing-persistance"))
    androidTestImplementation(project(":platform:testing:testing-rest"))

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

fun Project.httpEnabledForTesting(): Boolean =
    properties["testMode"]?.let { true } ?: false

fun Project.evaluateAPIUrl(): String =
    properties["testMode"]?.let { "http://localhost:4242" } ?: "https://api.chucknorris.io"

fun Project.evaluateTestBuildType(): String =
    properties["testMode"]?.let { "release" } ?: "debug"

fun BuildType.configureHttps() {
    buildConfigField("String", "CHUCKNORRIS_API_URL", "\"${project.evaluateAPIUrl()}\"")
    resValue("bool", "clear_networking_traffic_enabled", "${project.httpEnabledForTesting()}")
}

fun Project.loadSigningProperties(): Properties =
    Properties().apply {
        load(FileInputStream("$rootDir/signing.properties"))
    }
