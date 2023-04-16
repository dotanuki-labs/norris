package io.dotanuki.features.search.di

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.dotanuki.features.search.data.SearchesDataSource
import io.dotanuki.features.search.presentation.SearchViewModel
import io.dotanuki.features.search.ui.SearchEventsHandler
import io.dotanuki.platform.jvm.core.kodein.KodeinTags
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.provider

val searchModule = DI.Module("search") {

    bind {
        provider {
            @Suppress("UNCHECKED_CAST") val factory = object : ViewModelProvider.Factory {

                val dataSource = SearchesDataSource(
                    localStorage = instance(),
                    norrisClient = instance()
                )

                override fun <VM : ViewModel> create(modelClass: Class<VM>) =
                    SearchViewModel(dataSource) as VM
            }

            val host: FragmentActivity = instance(KodeinTags.hostActivity)
            ViewModelProvider(host, factory)[SearchViewModel::class.java]
        }
    }

    bind<SearchEventsHandler> {
        provider {
            SearchEventsHandler.Unidirectional(instance())
        }
    }
}
