package io.dotanuki.norris.search.di

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.dotanuki.norris.domain.FetchCategories
import io.dotanuki.norris.domain.services.SearchesHistoryService
import io.dotanuki.norris.features.utilties.KodeinTags
import io.dotanuki.norris.search.SearchViewModel
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.provider

val searchModule = DI.Module("search") {

    bind() from provider {
        @Suppress("UNCHECKED_CAST") val factory = object : ViewModelProvider.Factory {

            val fetchCategories = FetchCategories(
                categoriesCache = instance(),
                remoteFacts = instance()
            )

            val searchService = instance<SearchesHistoryService>()

            override fun <VM : ViewModel> create(klass: Class<VM>) =
                SearchViewModel(searchService, fetchCategories) as VM
        }

        val host: FragmentActivity = instance(KodeinTags.hostActivity)
        ViewModelProvider(host, factory).get(SearchViewModel::class.java)
    }
}
