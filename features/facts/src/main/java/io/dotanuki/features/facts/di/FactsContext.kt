package io.dotanuki.features.facts.di

import io.dotanuki.platform.android.core.persistence.LocalStorage
import io.dotanuki.platform.android.core.persistence.di.LocalStorageFactory
import io.dotanuki.platform.jvm.core.rest.RestClient
import io.dotanuki.platform.jvm.core.rest.di.ApiUrlFactory
import io.dotanuki.platform.jvm.core.rest.di.RestClientFactory

internal data class FactsContext(
    val restClient: RestClient,
    val localStorage: LocalStorage
) {

    companion object {

        context (ApiUrlFactory)
        fun standard() = FactsContext(
            restClient = RestClientFactory.create(),
            localStorage = LocalStorageFactory.create()
        )
    }
}
