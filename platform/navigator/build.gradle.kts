
plugins {
    id(BuildPlugins.Ids.androidModule)
}

dependencies {
    implementation(Libraries.kotlinStdlib)
    implementation(Libraries.appCompat)
    implementation(Libraries.kodein)
    implementation(project(":platform:shared-utilities"))

    testImplementation(project(":platform:coroutines-testutils"))
    testImplementation(Libraries.roboletric)
    testImplementation(Libraries.jUnit)
    testImplementation(Libraries.assertj)
}