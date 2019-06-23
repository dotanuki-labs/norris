package io.dotanuki.norris.facts.di

import io.dotanuki.norris.architecture.StateContainer
import io.dotanuki.norris.architecture.StateMachine
import io.dotanuki.norris.architecture.TaskExecutor
import io.dotanuki.norris.facts.FactPresentation
import io.dotanuki.norris.facts.FactsViewModel
import io.dotanuki.norris.rest.FetchFacts
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

        val stateMachine = StateMachine<List<FactPresentation>>(
            container = StateContainer.Unbounded(),
            executor = TaskExecutor.Concurrent(
                scope = instance(),
                dispatcher = Dispatchers.IO
            )
        )

        FactsViewModel(usecase, stateMachine)
    }
}