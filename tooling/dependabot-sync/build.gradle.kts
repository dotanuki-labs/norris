plugins {
    id("com.rickbusarow.gradle-dependency-sync") version "0.11.4"
}

dependencies {
    dependencySync("com.android.tools.build:gradle:7.0.3")
}
