package plugins

import conventions.ignoredVulnerabilities
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.sonatype.gradle.plugins.scan.ossindex.OssIndexPluginExtension
import org.sonatype.gradle.plugins.scan.ossindex.OutputFormat.JSON_CYCLONE_DX_1_4
import shadow.nexus.shadow.org.cyclonedx.model.Component.Type

class PlatformChecksPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.configureKtlint()
        target.configureDetekt()
        target.configureOssScan()
    }

    private fun Project.configureKtlint() {
        plugins.apply("org.jlleitschuh.gradle.ktlint")
    }

    private fun Project.configureDetekt() {
        plugins.apply("io.gitlab.arturbosch.detekt")

        val detektExtension = extensions.findByName("detekt") as DetektExtension

        detektExtension.run {
            config = files("$rootDir/detekt.yml")
            reports {
                xml.enabled = false
                html.enabled = false
                txt.enabled = false
            }
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
