package io.dotanuki.norris.facts.di

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.dotanuki.norris.domain.FetchFacts
import io.dotanuki.norris.domain.ManageSearchQuery
import io.dotanuki.norris.facts.FactsViewModel
import io.dotanuki.norris.features.utilties.KodeinTags
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.provider

val factsModule = DI.Module("menu_facts_list") {

    bind() from provider {
        @Suppress("UNCHECKED_CAST") val factory = object : ViewModelProvider.Factory {
            val fetchFacts = FetchFacts(
                factsService = instance()
            )

            val manageSearchQuery = ManageSearchQuery(
                historyService = instance()
            )

            override fun <VM : ViewModel> create(klass: Class<VM>) =
                FactsViewModel(fetchFacts, manageSearchQuery) as VM
        }

        val host: FragmentActivity = instance(KodeinTags.hostActivity)
        ViewModelProvider(host, factory).get(FactsViewModel::class.java)
    }
}