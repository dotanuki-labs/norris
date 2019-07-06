package io.dotanuki.norris.search.di

import io.dotanuki.norris.architecture.StateMachine
import io.dotanuki.norris.architecture.TaskExecutor
import io.dotanuki.norris.domain.ComposeSearchOptions
import io.dotanuki.norris.domain.FetchCategories
import io.dotanuki.norris.features.utilties.ConfigChangesAwareStateContainer
import io.dotanuki.norris.features.utilties.KodeinTags
import io.dotanuki.norris.search.SearchPresentation
import io.dotanuki.norris.search.SearchViewModel
import kotlinx.coroutines.Dispatchers
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

val searchModule = Kodein.Module("search") {

    bind() from provider {

        val fetchCategories = FetchCategories(
            categoriesCache = instance(),
            remoteFacts = instance()
        )
        val usecase = ComposeSearchOptions(
            searches = instance(),
            categories = fetchCategories
        )

        val stateContainer = ConfigChangesAwareStateContainer<SearchPresentation>(
            host = instance(KodeinTags.hostActivity)
        )

        val stateMachine = StateMachine(
            container = stateContainer,
            executor = TaskExecutor.Concurrent(
                scope = stateContainer.emissionScope,
                dispatcher = Dispatchers.IO
            )
        )

        SearchViewModel(usecase, stateMachine)
    }
}