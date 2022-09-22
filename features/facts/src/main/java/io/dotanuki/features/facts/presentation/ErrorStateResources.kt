package io.dotanuki.features.facts.presentation

import io.dotanuki.features.facts.domain.FactsRetrievalError
import io.dotanuki.platform.jvm.core.networking.errors.NetworkingError
import io.dotanuki.platform.jvm.core.networking.errors.RemoteServiceIntegrationError.RemoteSystem
import io.dotanuki.platform.android.core.assets.R as assetsR

data class ErrorStateResources(
    val image: Int,
    val message: Int
) {
    companion object {
        operator fun invoke(error: Throwable) =
            when (error) {
                is RemoteSystem -> ErrorStateResources(
                    assetsR.drawable.img_server_down,
                    assetsR.string.error_server_down
                )
                is NetworkingError -> ErrorStateResources(
                    assetsR.drawable.img_network_issue,
                    assetsR.string.error_network
                )
                is FactsRetrievalError.NoResultsFound -> ErrorStateResources(
                    assetsR.drawable.img_no_results,
                    assetsR.string.error_no_results
                )
                else -> ErrorStateResources(
                    assetsR.drawable.img_bug_found,
                    assetsR.string.error_bug_found
                )
            }
    }
}
