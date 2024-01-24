package io.dotanuki.features.facts.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.dotanuki.features.facts.presentation.FactsViewModel

context(FactsContext)
internal class FactsViewModelFactory : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <VM : ViewModel> create(modelClass: Class<VM>): VM =
        FactsViewModel() as VM

    // Shortcut for test purposes
    fun create(): FactsViewModel = create(FactsViewModel::class.java)
}
