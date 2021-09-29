import conventions.isTestMode

plugins {
    id("norris.modules.android.app")
    id("com.slack.keeper") version "0.11.1"
}

keeper {
    variantFilter {
        setIgnore(name != "release")
    }
}

android {
    defaultConfig {
        if (isTestMode()) {
            testInstrumentationRunnerArguments["listener"] = "leakcanary.FailTestOnLeakRunListener"
        }
    }
}

dependencies {
    implementation(projects.platform.core.coreRest)
    implementation(projects.platform.core.corePersistance)
    implementation(projects.platform.core.coreNavigator)
    implementation(projects.platform.common.commonKodein)
    implementation(projects.platform.common.commonStatic)
    implementation(projects.features.facts)
    implementation(projects.features.onboarding)
    implementation(projects.features.search)

    implementation("androidx.core:core:1.6.0")
    implementation("androidx.core:core-ktx:1.6.0")
    implementation("androidx.appcompat:appcompat:1.3.1")
    implementation("org.kodein.di:kodein-di-jvm:7.8.0")
    implementation("org.kodein.type:kodein-type-jvm:1.8.0")
    implementation("com.squareup.okhttp3:okhttp:4.9.1")

    if (isTestMode()) {
        releaseImplementation("com.squareup.leakcanary:leakcanary-android-release:2.7")
    }

    androidTestImplementation(projects.platform.testing.testingPersistance)
    androidTestImplementation(projects.platform.testing.testingRest)

    androidTestImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test:core:1.4.0")
    androidTestImplementation("androidx.test:core-ktx:1.4.0")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.ext:junit-ktx:1.1.3")
    androidTestImplementation("androidx.test:runner:1.4.0")
    androidTestImplementation("androidx.test:rules:1.4.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
    androidTestImplementation("com.google.truth:truth:1.1.3")
    androidTestImplementation("com.squareup.okhttp3:mockwebserver:4.9.1")

    androidTestImplementation("com.adevinta.android:barista:4.2.0") {
        exclude(group = "org.jetbrains.kotlin")
        exclude(group = "org.checkerframework")
    }
}
