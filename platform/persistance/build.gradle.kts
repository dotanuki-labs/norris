import dependencies.UnitTestDependencies.Companion.unitTest

plugins {
    id(BuildPlugins.Ids.androidModule)
}

dependencies {

    implementation(project(":platform:domain"))
    implementation(Libraries.coroutinesCore)
    implementation(Libraries.kodein)

    testImplementation(project(":platform:coroutines-testutils"))

    unitTest {
        forEachDependency { testImplementation(it) }
    }
}