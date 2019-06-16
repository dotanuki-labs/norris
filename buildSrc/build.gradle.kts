import org.gradle.kotlin.dsl.`kotlin-dsl`

repositories {
    jcenter()
}

plugins {
    `kotlin-dsl`
}

kotlinDslPluginOptions {
    experimentalWarning.set(false)
}