
import dependencies.UnitTestDependencies.Companion.unitTest

plugins {
    id(BuildPlugins.Ids.androidModule)
}

dependencies {
    implementation(Libraries.kotlinStdlib)
    implementation(Libraries.appCompat)
    implementation(Libraries.kodein)
    implementation(project(":platform:shared-utilities"))

    testImplementation(Libraries.roboletric)
    unitTest { forEachDependency { testImplementation(it) } }
}