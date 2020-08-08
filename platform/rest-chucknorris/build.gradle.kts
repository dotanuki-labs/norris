import dependencies.UnitTestDependencies.Companion.unitTest

plugins {
    id(BuildPlugins.Ids.kotlinModule)
    id(BuildPlugins.Ids.kotlinxSerialization)
}

dependencies {
    implementation(Libraries.kotlinStdlib)
    implementation(Libraries.okhttp)
    implementation(Libraries.okhttpLogger)
    implementation(Libraries.retrofit)
    implementation(Libraries.kotlinSerialization)
    implementation(Libraries.coroutinesCore)
    implementation(Libraries.kodein)

    implementation(project(":platform:logger"))
    implementation(project(":platform:domain"))
    implementation(project(":platform:networking"))

    unitTest {
        forEachDependency { testImplementation(it) }
    }
}