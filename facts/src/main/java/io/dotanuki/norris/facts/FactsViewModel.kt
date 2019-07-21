package io.dotanuki.norris.facts

import io.dotanuki.norris.architecture.StateMachine
import io.dotanuki.norris.architecture.StateTransition
import io.dotanuki.norris.architecture.UnsupportedUserInteraction
import io.dotanuki.norris.architecture.UserInteraction
import io.dotanuki.norris.architecture.UserInteraction.OpenedScreen
import io.dotanuki.norris.architecture.UserInteraction.RequestedFreshContent
import io.dotanuki.norris.domain.FetchFacts
import io.dotanuki.norris.domain.ManageSearchQuery

class FactsViewModel(
    private val factsFetcher: FetchFacts,
    private val queryManager: ManageSearchQuery,
    private val machine: StateMachine<FactsPresentation>
) {

    fun bind() = machine.states()

    fun handle(interaction: UserInteraction) =
        interpret(interaction)
            .let { transition ->
                machine.consume(transition)
            }

    private fun interpret(interaction: UserInteraction) =
        when (interaction) {
            OpenedScreen, RequestedFreshContent -> StateTransition(::showFacts)
            else -> throw UnsupportedUserInteraction
        }

    private suspend fun showFacts() =
        queryManager.actualQuery().let { query ->
            factsFetcher
                .search(query)
                .map { FactDisplayRow(it) }
                .let { rows ->
                    FactsPresentation(query, rows)
                }
        }
}