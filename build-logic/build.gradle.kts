import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.21"
    id("idea")
    id("org.gradle.java-gradle-plugin")
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions.jvmTarget = "11"
    kotlinOptions.freeCompilerArgs += listOf(
        "-Xopt-in=kotlin.time.ExperimentalTime",
        "-Xopt-in=kotlin.RequiresOptIn"
    )
}

gradlePlugin {
    plugins {
        create("catalog-sourcer") {
            id = "norris.plugins.catalogsourcer"
            implementationClass = "io.dotanuki.gradle.catalogsourcer.CatalogSourcerPlugin"
        }

        create("norris-platform-kotlin-module") {
            id = "norris.plugins.shapers.platform.kotlin"
            implementationClass = "io.dotanuki.gradle.shapers.PlatformKotlinModulePlugin"
        }

        create("norris-platform-android-module") {
            id = "norris.plugins.shapers.platform.android"
            implementationClass = "io.dotanuki.gradle.shapers.PlatformAndroidModulePlugin"
        }

        create("norris-android-feature-module") {
            id = "norris.plugins.shapers.feature"
            implementationClass = "io.dotanuki.gradle.shapers.FeatureModulePlugin"
        }

        create("norris-android-app-module") {
            id = "norris.plugins.shapers.app"
            implementationClass = "io.dotanuki.gradle.shapers.AndroidAppModulePlugin"
        }

        create("norris-root-module") {
            id = "norris.plugins.shapers.root"
            implementationClass = "io.dotanuki.gradle.shapers.RootModulePlugin"
        }
    }
}

// Small tricky to fool Dependabot head !!!

// Function used as a fake Gradle configuration
fun dependabot(target: String, alias: () -> String): String = target.apply {
    project.logger.info("[${alias()}] → $this")
}

// We need all possible Maven repos the upstream project uses here,
// since Dependabot will parse this list !!!
repositories {
    mavenCentral()
    google()
    gradlePluginPortal()
    maven(url = "https://maven.pkg.jetbrains.space/public/p/kotlinx-html/maven")
}

dependencies {

    implementation(gradleApi())
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("android-extensions"))

    val agp = dependabot("com.android.tools.build:gradle:7.1.3") { "android-gradle-plugin" }
    val kgp = dependabot("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.21") { "kotlin-gradle-plugin" }
    val tgp = dependabot("com.adarshr:gradle-test-logger-plugin:3.2.0") { "testlogger-gradle-plugin" }
    val keeper = dependabot("com.slack.keeper:keeper:0.11.2") { "keeper-gradle-plugin" }
    val ossAudit = dependabot("org.sonatype.gradle.plugins:scan-gradle-plugin:2.3.0") { "oss-audit-gradle-plugin" }
    val detekt = dependabot("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:1.20.0") { "detekt-gradle-plugin" }
    val ktlint = dependabot("org.jlleitschuh.gradle:ktlint-gradle:10.3.0") { "ktlint-gradle-plugin" }

    implementation(agp)
    implementation(kgp)
    implementation(tgp)
    implementation(keeper)
    implementation(ossAudit)
    implementation(detekt)
    implementation(ktlint)

    // Gradle plugins
    dependabot("org.jetbrains.kotlin:kotlin-serialization:1.6.21") { "kotlinx-serialization-gradle-plugin" }
    dependabot("com.karumi:shot:5.14.1") { "shot-gradle-plugin" }

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
    dependabot("com.squareup.okhttp3:okhttp:4.9.3") { "okhttp-core" }
    dependabot("com.squareup.okhttp3:logging-interceptor:4.9.3") { "okhttp-logging" }
    dependabot("com.squareup.retrofit2:retrofit:2.9.0") { "retrofit" }
    dependabot("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:0.8.0") { "jw-retrofit-kotlinx-serialisation" }

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
    dependabot("com.karumi:shot-android:5.14.1") { "shot-android" }
    dependabot("com.squareup.leakcanary:leakcanary-android-release:2.9.1") { "leak-canary-release" }
    dependabot("androidx.test.espresso:espresso-core:3.4.0") { "espresso-core" }
    dependabot("com.adevinta.android:barista:4.2.0") { "barista" }
    dependabot("com.squareup.radiography:radiography:2.4.1") { "radiography" }
}
