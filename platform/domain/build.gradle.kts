import dependencies.UnitTestDependencies.Companion.unitTest

plugins {
    id(BuildPlugins.Ids.kotlinModule)
}

dependencies {
    implementation(Libraries.kotlinStdlib)
    implementation(Libraries.coroutinesCore)

    unitTest {
        forEachDependency { testImplementation(it) }
    }

    testImplementation(project(":platform:coroutines-testutils"))
}