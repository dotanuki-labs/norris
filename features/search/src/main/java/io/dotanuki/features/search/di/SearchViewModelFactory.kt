package io.dotanuki.features.search.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.dotanuki.features.search.data.SearchesDataSource
import io.dotanuki.features.search.presentation.SearchViewModel
import io.dotanuki.platform.android.core.persistance.di.LocalStorageFactory
import io.dotanuki.platform.jvm.core.rest.di.ApiUrlFactory
import io.dotanuki.platform.jvm.core.rest.di.ChuckNorrisServiceClientFactory

context(ApiUrlFactory)
internal class SearchViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <VM : ViewModel> create(modelClass: Class<VM>): VM {
        val localStorage = LocalStorageFactory.create()
        val chuckNorrisServiceClient = ChuckNorrisServiceClientFactory.create()
        val dataSource = SearchesDataSource(localStorage, chuckNorrisServiceClient)
        return SearchViewModel(dataSource) as VM
    }
}
