package io.dotanuki.norris.facts.di

import io.dotanuki.norris.architecture.StateMachine
import io.dotanuki.norris.architecture.TaskExecutor
import io.dotanuki.norris.domain.FetchFacts
import io.dotanuki.norris.domain.ManageSearchQuery
import io.dotanuki.norris.facts.FactsPresentation
import io.dotanuki.norris.facts.FactsViewModel
import io.dotanuki.norris.features.utilties.ConfigChangesAwareStateContainer
import io.dotanuki.norris.features.utilties.KodeinTags
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.provider

val factsModule = DI.Module("menu_facts_list") {

    bind() from provider {

        val fetchFacts = FetchFacts(
            factsService = instance()
        )

        val stateContainer = ConfigChangesAwareStateContainer<FactsPresentation>(
            host = instance(KodeinTags.hostActivity)
        )

        val manageSearchQuery = ManageSearchQuery(
            historyService = instance()
        )

        val stateMachine = StateMachine(
            container = stateContainer,
            executor = TaskExecutor.Concurrent(
                scope = stateContainer.emissionScope,
                dispatcher = instance()
            )
        )

        FactsViewModel(fetchFacts, manageSearchQuery, stateMachine)
    }
}