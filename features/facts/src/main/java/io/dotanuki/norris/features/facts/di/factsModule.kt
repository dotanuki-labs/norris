package io.dotanuki.norris.features.facts.di

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.dotanuki.norris.features.facts.data.ActualSearchDataSource
import io.dotanuki.norris.features.facts.data.FactsDataSource
import io.dotanuki.norris.features.facts.presentation.FactsViewModel
import io.dotanuki.norris.features.facts.ui.FactsEventsHandler
import io.dotanuki.platform.android.core.navigator.Navigator
import io.dotanuki.platform.jvm.core.kodein.KodeinTags
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

                override fun <VM : ViewModel> create(modelClass: Class<VM>) =
                    FactsViewModel(factsDataSource, actualSearchDataSource) as VM
            }

            val host: FragmentActivity = instance(KodeinTags.hostActivity)
            ViewModelProvider(host, factory)[FactsViewModel::class.java]
        }
    }

    bind<FactsEventsHandler> {
        provider {
            val viewModel = instance<FactsViewModel>()
            val navigator = instance<Navigator>()
            FactsEventsHandler.Unidirectional(viewModel, navigator)
        }
    }
}
