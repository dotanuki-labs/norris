package io.dotanuki.norris.facts.di

import io.dotanuki.norris.architecture.StateMachine
import io.dotanuki.norris.architecture.TaskExecutor
import io.dotanuki.norris.facts.FactsPresentation
import io.dotanuki.norris.facts.FactsViewModel
import io.dotanuki.norris.features.utilties.ConfigChangesAwareStateContainer
import io.dotanuki.norris.features.utilties.KodeinTags
import io.dotanuki.norris.domain.FetchFacts
import kotlinx.coroutines.Dispatchers
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

val factsModule = Kodein.Module("facts") {

    bind() from provider {

        val usecase = FetchFacts(
            service = instance()
        )

        val stateContainer = ConfigChangesAwareStateContainer<FactsPresentation>(
            host = instance(KodeinTags.hostActivity)
        )

        val stateMachine = StateMachine(
            container = stateContainer,
            executor = TaskExecutor.Concurrent(
                scope = stateContainer.emissionScope,
                dispatcher = Dispatchers.IO
            )
        )

        FactsViewModel(usecase, stateMachine)
    }
}