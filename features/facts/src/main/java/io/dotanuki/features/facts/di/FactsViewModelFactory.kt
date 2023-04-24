package io.dotanuki.features.facts.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.dotanuki.features.facts.data.ActualSearchDataSource
import io.dotanuki.features.facts.data.FactsDataSource
import io.dotanuki.features.facts.presentation.FactsViewModel

@Suppress("UNCHECKED_CAST")
class FactsViewModelFactory(
    private val factsDataSource: FactsDataSource,
    private val actualSearchDataSource: ActualSearchDataSource
) : ViewModelProvider.Factory {

    override fun <VM : ViewModel> create(modelClass: Class<VM>): VM =
        FactsViewModel(factsDataSource, actualSearchDataSource) as VM
}
