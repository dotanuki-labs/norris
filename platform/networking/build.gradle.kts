import dependencies.UnitTestDependencies.Companion.unitTest

plugins {
    id(BuildPlugins.Ids.kotlinModule)
}

dependencies {
    implementation(Libraries.kotlinStdlib)
    implementation(Libraries.kotlinSerialization)

    implementation(Libraries.okhttp)
    implementation(Libraries.okhttpLogger)
    implementation(Libraries.retrofit)
    implementation(Libraries.retrofitKotlinSerialization)
    implementation(Libraries.coroutinesCore)

    implementation(Libraries.kodein)

    implementation(project(":platform:logger"))
    implementation(project(":platform:domain"))

    unitTest {
        forEachDependency { testImplementation(it) }
    }

    testImplementation(project(":platform:coroutines-testutils"))
}