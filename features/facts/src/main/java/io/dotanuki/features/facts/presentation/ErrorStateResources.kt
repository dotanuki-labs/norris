package io.dotanuki.features.facts.presentation

import io.dotanuki.features.facts.domain.FactsRetrievalError
import io.dotanuki.platform.jvm.core.rest.HttpNetworkingError
import io.dotanuki.platform.android.core.assets.R as assetsR

data class ErrorStateResources(
    val image: Int,
    val message: Int,
) {
    companion object {
        operator fun invoke(error: Throwable) =
            when (error) {
                is HttpNetworkingError.Restful.Server ->
                    ErrorStateResources(
                        assetsR.drawable.img_server_down,
                        assetsR.string.error_server_down
                    )
                is HttpNetworkingError.Connectivity ->
                    ErrorStateResources(
                        assetsR.drawable.img_network_issue,
                        assetsR.string.error_network
                    )
                is FactsRetrievalError.NoResultsFound ->
                    ErrorStateResources(
                        assetsR.drawable.img_no_results,
                        assetsR.string.error_no_results
                    )
                else ->
                    ErrorStateResources(
                        assetsR.drawable.img_bug_found,
                        assetsR.string.error_bug_found
                    )
            }
    }
}
