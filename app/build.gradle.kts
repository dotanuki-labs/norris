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
    implementation(projects.platform.common.commonAndroid)
    implementation(projects.platform.common.commonKodein)
    implementation(projects.platform.common.commonStatic)
    implementation(projects.features.facts)
    implementation(projects.features.search)

    implementation(Deps.kodeinDi)
    implementation(Deps.kodeinType)
    implementation(Deps.coroutinesCore)
    implementation(Deps.coroutinesJvm)
    implementation(Deps.coroutinesAndroid)

    implementation(Deps.androidxTestCore)
    implementation(Deps.androidxTestCoreKtx)
    implementation(Deps.androidxAppCompat)
    implementation(Deps.androidxLifecycleCommon)
    implementation(Deps.androidxLifecycleRuntime)
    implementation(Deps.okHttp)

    if (isTestMode()) {
        releaseImplementation(Deps.leakCanary)
    }

    androidTestImplementation(projects.platform.testing.testingPersistance)
    androidTestImplementation(projects.platform.testing.testingRest)

    androidTestImplementation(Deps.junit4)
    androidTestImplementation(Deps.okHttp)
    androidTestImplementation(Deps.androidxTestCore)
    androidTestImplementation(Deps.androidxTestCoreKtx)
    androidTestImplementation(Deps.androidxTestExtJunit)
    androidTestImplementation(Deps.androidxTestExtJunitKtx)
    androidTestImplementation(Deps.androidxTestRunner)
    androidTestImplementation(Deps.androidxTestRules)
    androidTestImplementation(Deps.okHttpMockWebServer)
    androidTestImplementation(Deps.espressoCore)

    androidTestImplementation(Deps.barista) {
        exclude(group = "org.jetbrains.kotlin")
        exclude(group = "org.checkerframework")
    }
}
