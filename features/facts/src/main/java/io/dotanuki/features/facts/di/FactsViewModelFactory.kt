package io.dotanuki.features.facts.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.dotanuki.features.facts.data.ActualSearchDataSource
import io.dotanuki.features.facts.data.FactsDataSource
import io.dotanuki.features.facts.presentation.FactsViewModel
import io.dotanuki.platform.android.core.persistance.di.LocalStorageFactory
import io.dotanuki.platform.jvm.core.rest.di.ApiUrlFactory
import io.dotanuki.platform.jvm.core.rest.di.ChuckNorrisServiceClientFactory

context(ApiUrlFactory, ChuckNorrisServiceClientFactory, LocalStorageFactory)
internal class FactsViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <VM : ViewModel> create(modelClass: Class<VM>): VM {
        val localStorage = this@LocalStorageFactory.create()
        val chuckNorrisServiceClient = this@ChuckNorrisServiceClientFactory.create()
        val factsDataSource = FactsDataSource(chuckNorrisServiceClient)
        val actualSearchDataSource = ActualSearchDataSource(localStorage)
        return FactsViewModel(factsDataSource, actualSearchDataSource) as VM
    }
}
