package io.dotanuki.norris.facts.di

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.dotanuki.norris.facts.data.ActualSearchDataSource
import io.dotanuki.norris.facts.data.FactsDataSource
import io.dotanuki.norris.facts.presentation.FactsViewModel
import io.dotanuki.norris.facts.ui.FactsScreen
import io.dotanuki.norris.facts.ui.WrappedContainer
import io.dotanuki.norris.features.utilties.KodeinTags
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.provider

val factsModule = DI.Module("facts-module") {

    bind {
        provider {
            @Suppress("UNCHECKED_CAST") val factory = object : ViewModelProvider.Factory {
                val actualSearchDataSource = ActualSearchDataSource(
                    storage = instance()
                )

                val factsDataSource = FactsDataSource(
                    api = instance()
                )

                override fun <VM : ViewModel> create(klass: Class<VM>) =
                    FactsViewModel(factsDataSource, actualSearchDataSource) as VM
            }

            val host: FragmentActivity = instance(KodeinTags.hostActivity)
            ViewModelProvider(host, factory).get(FactsViewModel::class.java)
        }
    }

    bind<FactsScreen> {
        provider {
            WrappedContainer()
        }
    }
}
