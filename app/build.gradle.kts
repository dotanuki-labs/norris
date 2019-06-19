import configs.AndroidConfig
import configs.KotlinConfig
import configs.ProguardConfig
import modules.ModuleNames

plugins {
    id(BuildPlugins.Ids.androidApplication)
    id(BuildPlugins.Ids.kotlinAndroid)
    id(BuildPlugins.Ids.kotlinAndroidExtensions)
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
            versionNameSuffix = "-debug"
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

    kotlinOptions {
        extensions.add("jvmTarget", KotlinConfig.targetJVM)
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
    implementation(project(ModuleNames.Logger))
    implementation(project(ModuleNames.Domain))
    implementation(project(ModuleNames.Features.SharedAssets))
    implementation(project(ModuleNames.Features.Facts))

    testImplementation(Libraries.jUnit)
    androidTestImplementation(Libraries.espressoCore)
    androidTestImplementation(Libraries.androidTestRules)
}

androidExtensions {
    extensions.add("experimental", true)
}