package io.dotanuki.platform.jvm.core.rest.util

import eu.rekawek.toxiproxy.Proxy
import eu.rekawek.toxiproxy.model.Toxic
import eu.rekawek.toxiproxy.model.ToxicDirection

fun Toxic.setToxicity(level: ToxicityLevel) = apply {
    toxicity = level.value
}

fun Proxy.limitData(numberOfBytes: Int): Toxic =
    toxics().limitData("limit-data", ToxicDirection.DOWNSTREAM, numberOfBytes.toLong())

fun Proxy.bandwidth(latency: Int, jitter: Int): Toxic =
    toxics()
        .latency("induced-jitter", ToxicDirection.DOWNSTREAM, latency.toLong())
        .setJitter(jitter.toLong())

fun Proxy.timeout(delay: Long): Toxic =
    toxics().timeout("timeout", ToxicDirection.DOWNSTREAM, delay)
