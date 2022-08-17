package io.dotanuki.norris.search.di

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.dotanuki.norris.common.kodein.KodeinTags
import io.dotanuki.norris.search.data.SearchesDataSource
import io.dotanuki.norris.search.presentation.SearchViewModel
import io.dotanuki.norris.search.ui.SearchScreen
import io.dotanuki.norris.search.ui.WrappedContainer
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.provider

val searchModule = DI.Module("search") {

    bind {
        provider {
            @Suppress("UNCHECKED_CAST") val factory = object : ViewModelProvider.Factory {

                val dataSource = SearchesDataSource(
                    storage = instance(),
                    api = instance()
                )

                override fun <VM : ViewModel> create(modelClass: Class<VM>) =
                    SearchViewModel(dataSource) as VM
            }

            val host: FragmentActivity = instance(KodeinTags.hostActivity)
            ViewModelProvider(host, factory).get(SearchViewModel::class.java)
        }
    }

    bind<SearchScreen> {
        provider {
            WrappedContainer()
        }
    }
}
