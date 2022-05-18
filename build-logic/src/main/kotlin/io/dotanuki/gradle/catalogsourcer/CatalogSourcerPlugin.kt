package io.dotanuki.gradle.catalogsourcer

import org.gradle.api.Plugin
import org.gradle.api.initialization.Settings

@Suppress("UnstableApiUsage")
class CatalogSourcerPlugin : Plugin<Settings> {
    override fun apply(target: Settings) {

        target.dependencyResolutionManagement.versionCatalogs { catalogsContainer ->
            catalogsContainer.create("libs") { catalogBuilder ->
                DependabotBridge.extractDependencies().forEach { (alias, coordinate) ->
                    catalogBuilder.library(alias, coordinate)
                }
            }
        }
    }
}
