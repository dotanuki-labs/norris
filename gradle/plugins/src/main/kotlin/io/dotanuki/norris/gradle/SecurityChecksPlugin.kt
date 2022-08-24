package io.dotanuki.norris.gradle

import io.dotanuki.norris.gradle.internal.conventions.ignoredVulnerabilities
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.sonatype.gradle.plugins.scan.ossindex.OssIndexPluginExtension
import org.sonatype.gradle.plugins.scan.ossindex.OutputFormat.JSON_CYCLONE_DX_1_4
import shadow.nexus.shadow.org.cyclonedx.model.Component.Type

class SecurityChecksPlugin : Plugin<Project> {

    override fun apply(target: Project) {

        val isCI = System.getenv().containsKey("CI")

        if (isCI) {
            target.configureOssScan()
        }
    }

    private fun Project.configureOssScan() {
        plugins.apply("org.sonatype.gradle.plugins.scan")

        val ossAuditExtension = extensions.findByName("ossIndexAudit") as OssIndexPluginExtension

        ossAuditExtension.run {
            isAllConfigurations = true
            isPrintBanner = false
            isShowAll = true
            excludeVulnerabilityIds = ignoredVulnerabilities()
            outputFormat = JSON_CYCLONE_DX_1_4
            cycloneDxComponentType = Type.APPLICATION
        }
    }
}