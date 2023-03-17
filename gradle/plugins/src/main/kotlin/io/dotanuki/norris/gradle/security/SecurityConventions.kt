package io.dotanuki.norris.gradle.security

import org.gradle.api.Project

private val flaggedDependencies = mapOf(
    "sonatype-2018-0705" to "commons-io",
    "sonatype-2020-0641" to "grpc-core",
    "sonatype-2020-0926" to "guava",
    "CVE-2021-22569" to "protobuf-java",
    "CVE-2021-37714" to "jsoup",
    "CVE-2019-9512" to "netty-codec-http2",
    "CVE-2019-9514" to "netty-codec-http2",
    "CVE-2019-9515" to "netty-codec-http2",
    "CVE-2019-9518" to "netty-codec-http2",
    "CVE-2021-21295" to "netty-codec-http2",
    "CVE-2019-20444" to "netty-codec-http",
    "sonatype-2020-1031" to "netty-codec-http",
    "CVE-2021-43797" to "netty-codec-http",
    "CVE-2021-21295" to "netty-codec-http",
    "CVE-2021-21290" to "netty-codec-http",
    "CVE-2019-20445" to "netty-codec-http",
    "CVE-2019-16869" to "netty-codec-http",
    "sonatype-2020-0026" to "netty-handler",
    "CVE-2021-21290" to "netty-handler",
    "CVE-2021-21290" to "netty-handler",
    "sonatype-2021-0789" to "netty-codec",
    "CVE-2021-37136" to "netty-codec",
    "CVE-2021-37137" to "netty-codec",
    "CVE-2021-21290" to "netty-common",
    "CVE-2020-15250" to "junit",
    "CVE-2022-36033" to "jsoup",
    "sonatype-2021-4916" to "bcprov-jdk15on",
    "sonatype-2021-1694" to "gson"
)

fun Project.ignoredVulnerabilities(): Set<String> =
    flaggedDependencies.let {
        logger.lifecycle("")
        logger.lifecycle("Security Checks Plugin:")
        logger.lifecycle("Ignoring CVE/Sonatype advisories for some build dependencies.")
        logger.lifecycle("Reason : they are not packaged in the final APK")
        logger.lifecycle("")

        it.map { entry -> entry.key }.toSet().onEach { id ->
            logger.lifecycle(" • https://ossindex.sonatype.org/vulnerability/$id")
        }
    }
