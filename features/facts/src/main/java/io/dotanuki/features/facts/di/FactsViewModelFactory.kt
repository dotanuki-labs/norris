package io.dotanuki.features.facts.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.dotanuki.features.facts.data.ActualSearchDataSource
import io.dotanuki.features.facts.data.FactsDataSource
import io.dotanuki.features.facts.presentation.FactsViewModel

class FactsViewModelFactory(
    val factsDataSource: FactsDataSource,
    val actualSearchDataSource: ActualSearchDataSource
) : ViewModelProvider.Factory {

    override fun <VM : ViewModel> create(modelClass: Class<VM>): VM =
        FactsViewModel(factsDataSource, actualSearchDataSource) as VM
}
