package io.dotanuki.features.search.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.dotanuki.features.search.data.SearchesDataSource
import io.dotanuki.features.search.presentation.SearchViewModel
import io.dotanuki.platform.android.core.persistance.LocalStorage
import io.dotanuki.platform.jvm.core.rest.ChuckNorrisServiceClient

@Suppress("UNCHECKED_CAST")
class SearchViewModelFactory(
    private val localStorage: LocalStorage,
    private val chuckNorrisServiceClient: ChuckNorrisServiceClient
) : ViewModelProvider.Factory {

    override fun <VM : ViewModel> create(modelClass: Class<VM>): VM {
        val dataSource = SearchesDataSource(localStorage, chuckNorrisServiceClient)
        return SearchViewModel(dataSource) as VM
    }
}
