package io.dotanuki.features.facts.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.dotanuki.features.facts.data.FactsDataSource
import io.dotanuki.features.facts.presentation.FactsViewModel
import io.dotanuki.platform.android.core.persistance.di.LocalStorageFactory
import io.dotanuki.platform.jvm.core.rest.di.ApiUrlFactory
import io.dotanuki.platform.jvm.core.rest.di.ChuckNorrisServiceClientFactory

context(ApiUrlFactory)
internal class FactsViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <VM : ViewModel> create(modelClass: Class<VM>): VM {
        val localStorage = LocalStorageFactory.create()
        val serviceClient = ChuckNorrisServiceClientFactory.create()
        val dataSource = FactsDataSource(serviceClient, localStorage)
        return FactsViewModel(dataSource) as VM
    }
}
