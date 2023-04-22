package io.dotanuki.features.search.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.dotanuki.features.search.data.SearchesDataSource
import io.dotanuki.features.search.presentation.SearchViewModel

class SearchViewModelFactory(
    private val searchesDataSource: SearchesDataSource
) : ViewModelProvider.Factory {

    override fun <VM : ViewModel> create(modelClass: Class<VM>): VM =
        SearchViewModel(searchesDataSource) as VM
}
