import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
}

plugins {
    java
    `kotlin-dsl`
    `java-gradle-plugin`
}

buildscript {
    repositories {
        google()
        mavenCentral()
    }
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions.jvmTarget = "11"
    kotlinOptions.freeCompilerArgs += listOf(
        "-Xopt-in=kotlin.time.ExperimentalTime",
        "-Xopt-in=kotlin.RequiresOptIn"
    )
}

tasks.withType<JavaCompile>().configureEach {
    targetCompatibility = JavaVersion.VERSION_11.toString()
    sourceCompatibility = JavaVersion.VERSION_11.toString()
}

gradlePlugin {
    plugins {
        register("kotlin-module-plugin") {
            id = "norris.modules.kotlin"
            implementationClass = "plugins.NorrisKotlinModulePlugin"
        }

        register("android-platform-module-plugin") {
            id = "norris.modules.android.platform"
            implementationClass = "plugins.NorrisAndroidPlatformModulePlugin"
        }

        register("android-feature-module-plugin") {
            id = "norris.modules.android.feature"
            implementationClass = "plugins.NorrisAndroidFeatureModulePlugin"
        }

        register("android-application-module-plugin") {
            id = "norris.modules.android.app"
            implementationClass = "plugins.NorrisAndroidApplicationModulePlugin"
        }
    }
}


// Small tricky to fool Dependabot head !!!

// Function used as a fake Gradle configuration
fun dependabot(target: String, alias: () -> String): String = target.apply {
    project.logger.info("[${alias()}] → $this")
}

// We need all possible Maven repos this project uses here, since Dependabot will parse this list
repositories {
    mavenCentral()
    google()
    gradlePluginPortal()
    maven(url = "https://maven.pkg.jetbrains.space/public/p/kotlinx-html/maven")
}

// All project dependencies will be listed below,
// although only a few of them are required for buildSrc stuff
dependencies {

    project.logger.lifecycle("This project manages dependencies with Dependabot")

    // Used by plugins implemented at buildSrc and also required as classpath dependency in other
    // Gradle build scripts, eg <root-project>/build.gradle.kts
    // Note that not all Gradle plugins in this project use the legacy convention

    val agp = dependabot("com.android.tools.build:gradle:7.1.3") { "android-gradle-plugin" }
    val kgp = dependabot("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.21") { "kotlin-gradle-plugin" }
    val tgp = dependabot("com.adarshr:gradle-test-logger-plugin:3.2.0") { "testlogger-gradle-plugin" }
    val keeper = dependabot("com.slack.keeper:keeper:0.11.2") { "keeper-gradle-plugin" }

    implementation(agp)
    implementation(kgp)
    implementation(tgp)
    implementation(keeper)
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("android-extensions"))

    // Used on Gradle scripts outside buildSrc,
    // either as build script dependency or some other configuration

    // Gradle plugins
    dependabot("org.jetbrains.kotlin:kotlin-serialization:1.6.21") { "kotlinx-serialization-gradle-plugin" }
    dependabot("com.karumi:shot:5.14.0") { "shot-gradle-plugin" }

    // Kodein
    dependabot("org.kodein.di:kodein-di-jvm:7.11.0") { "kodein-di-jvm" }
    dependabot("org.kodein.type:kodein-type-jvm:1.12.0") { "kodein-type-jvm" }

    // Kotlinx
    dependabot("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.1") { "kotlinx-coroutines-core" }
    dependabot("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.6.1") { "kotlinx-coroutines-jvm" }
    dependabot("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.1") { "kotlinx-coroutines-android" }
    dependabot("org.jetbrains.kotlinx:kotlinx-serialization-core:1.3.3") { "kotlinx-serialization-core" }
    dependabot("org.jetbrains.kotlinx:kotlinx-serialization-core-jvm:1.3.3") { "kotlinx-serialization-jvm" }
    dependabot("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.3") { "kotlinx-serialization-json" }

    // Androidx
    dependabot("androidx.core:core:1.7.0") { "androidx-core" }
    dependabot("androidx.core:core-ktx:1.7.0") { "androidx-core-ktx" }
    dependabot("androidx.lifecycle:lifecycle-common:2.4.1") { "androidx-lifecycle-common" }
    dependabot("androidx.lifecycle:lifecycle-runtime-ktx:2.4.1") { "androidx-lifecycle-runtime" }
    dependabot("androidx.lifecycle:lifecycle-viewmodel:2.4.1") { "androidx-lifecycle-vm" }
    dependabot("androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.1") { "androidx-lifecycle-vm-ktx" }
    dependabot("androidx.appcompat:appcompat:1.4.1") { "androidx-appcompat" }
    dependabot("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0") { "androidx-swipetorefresh" }
    dependabot("androidx.coordinatorlayout:coordinatorlayout:1.2.0") { "androidx-coordinatorlayout" }
    dependabot("androidx.recyclerview:recyclerview:1.2.1") { "androidx-recyclerview" }
    dependabot("com.google.android.material:material:1.6.0") { "google-material-design" }

    // Networking
    dependabot("com.squareup.okhttp3:okhttp:4.9.3") { "square-okhttp" }
    dependabot("com.squareup.okhttp3:logging-interceptor:4.9.3") { "square-okhttp-logging" }
    dependabot("com.squareup.retrofit2:retrofit:2.9.0") { "square-retrofit" }
    dependabot("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:0.8.0") { "retrofit-kotlinx-converter" }

    // Testing
    dependabot("junit:junit:4.13.2") { "junit4" }
    dependabot("org.slf4j:slf4j-nop:1.7.36") { "slf4j" }
    dependabot("org.robolectric:robolectric:4.8.1") { "robolectric" }
    dependabot("com.google.truth:truth:1.1.3") { "truth" }
    dependabot("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.1") { "kotlinx-coroutines-test" }
    dependabot("androidx.test:core:1.4.0") { "androidx-test-core" }
    dependabot("androidx.test:core-ktx:1.4.0") { "androidx-test-corektx" }
    dependabot("androidx.test.ext:junit:1.1.3") { "androidx-testext-junit" }
    dependabot("androidx.test.ext:junit-ktx:1.1.3") { "androidx-testext-junitktx" }
    dependabot("androidx.test:runner:1.4.0") { "androidx-test-runner" }
    dependabot("androidx.test:rules:1.4.0") { "androidx-test-rules" }
    dependabot("androidx.test:monitor:1.5.0") { "androidx-test-monitor" }
    dependabot("com.squareup.okhttp3:mockwebserver:4.9.3") { "okhttp-mockwebserver" }
    dependabot("com.karumi:shot-android:5.14.0") { "shot-android" }
    dependabot("com.squareup.leakcanary:leakcanary-android-release:2.9.1") { "leak-canary-release" }
    dependabot("androidx.test.espresso:espresso-core:3.4.0") { "espresso-core" }
    dependabot("com.adevinta.android:barista:4.2.0") { "barista" }
    dependabot("com.squareup.radiography:radiography:2.4.1") { "radiography" }
}
