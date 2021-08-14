
plugins {
    id("norris.modules.android.app")
    id("com.slack.keeper") version "0.11.0"
}

repositories {
    google()
}

dependencies {
    implementation(project(":platform:core:core-rest"))
    implementation(project(":platform:core:core-persistance"))
    implementation(project(":platform:core:core-navigator"))
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
