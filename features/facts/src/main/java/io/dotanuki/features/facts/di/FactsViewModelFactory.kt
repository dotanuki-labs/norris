package io.dotanuki.features.facts.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.dotanuki.features.facts.data.ActualSearchDataSource
import io.dotanuki.features.facts.data.FactsDataSource
import io.dotanuki.features.facts.presentation.FactsViewModel
import io.dotanuki.platform.android.core.persistance.LocalStorage
import io.dotanuki.platform.jvm.core.rest.ChuckNorrisServiceClient

@Suppress("UNCHECKED_CAST")
class FactsViewModelFactory(
    private val localStorage: LocalStorage,
    private val chuckNorrisServiceClient: ChuckNorrisServiceClient
) : ViewModelProvider.Factory {

    override fun <VM : ViewModel> create(modelClass: Class<VM>): VM {
        val factsDataSource = FactsDataSource(chuckNorrisServiceClient)
        val actualSearchDataSource = ActualSearchDataSource(localStorage)
        return FactsViewModel(factsDataSource, actualSearchDataSource) as VM
    }
}
