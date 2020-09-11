
plugins {
    id(BuildPlugins.Ids.kotlinModule)
}

dependencies {
    implementation(project(":platform:logger"))
    implementation(Libraries.coroutinesCore)

    testImplementation(project(":platform:coroutines-testutils"))
    testImplementation(Libraries.jUnit)
    testImplementation(Libraries.assertj)
    testImplementation(Libraries.turbine)
}