package io.dotanuki.norris.gradle.internal.conventions

import org.gradle.api.Project

private val vulnerabilitiesTracking = mapOf(
    "sonatype-2018-0705" to "commons-io not packaged in the final product",
    "sonatype-2020-0641" to "grpc-core not packaged in the final product",
    "sonatype-2020-0926" to "guava not packaged in the final product",
    "CVE-2021-22569" to "protobuf-java not packaged in the final product",
    "CVE-2021-37714" to "jsoup not packaged in the final product",
    "CVE-2019-9512" to "netty-codec-http2 not packaged in the final product",
    "CVE-2019-9514" to "netty-codec-http2 not packaged in the final product",
    "CVE-2019-9515" to "netty-codec-http2 not packaged in the final product",
    "CVE-2019-9518" to "netty-codec-http2 not packaged in the final product",
    "CVE-2021-21295" to "netty-codec-http2 not packaged in the final product",
    "CVE-2019-20444" to "netty-codec-http not packaged in the final product",
    "sonatype-2020-1031" to "netty-codec-http not packaged in the final product",
    "CVE-2021-43797" to "netty-codec-http not packaged in the final product",
    "CVE-2021-21295" to "netty-codec-http not packaged in the final product",
    "CVE-2021-21290" to "netty-codec-http not packaged in the final product",
    "CVE-2019-20445" to "netty-codec-http not packaged in the final product",
    "CVE-2019-16869" to "netty-codec-http not packaged in the final product",
    "sonatype-2020-0026" to "netty-handler not packaged in the final product",
    "CVE-2021-21290" to "netty-handler not packaged in the final product",
    "CVE-2021-21290" to "netty-handler not packaged in the final product",
    "sonatype-2021-0789" to "netty-codec not packaged in the final product",
    "CVE-2021-37136" to "netty-codec not packaged in the final product",
    "CVE-2021-37137" to "netty-codec not packaged in the final product",
    "CVE-2021-21290" to "netty-common not packaged in the final product",
    "CVE-2020-15250" to "junit not packaged in the final product",
    "sonatype-2021-4916" to "bcprov-jdk15on not packaged in the final product",
    "sonatype-2021-1694" to "gson not packaged in the final product"
)

fun Project.ignoredVulnerabilities(): Set<String> =
    vulnerabilitiesTracking.let {
        logger.lifecycle("This project deliberately ignores CVEs for some of its transistive dependencies")
        it.entries
            .onEach { (id, reason) -> logger.info(formattedMessage(id, reason)) }
            .map { entry -> entry.key }
            .toSet()
    }

private fun formattedMessage(vulnerabilityId: String, reason: String): String =
    """
    Ignoring : https://ossindex.sonatype.org/vulnerability/$vulnerabilityId
    Reason : $reason
    
    """.trimIndent()
