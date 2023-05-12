package io.dotanuki.features.search.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.dotanuki.features.search.presentation.SearchViewModel

context(SearchContext)
internal class SearchViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <VM : ViewModel> create(modelClass: Class<VM>): VM =
        SearchViewModel() as VM

    // Shortcut for test purposes
    fun create(): SearchViewModel = create(SearchViewModel::class.java)
}
