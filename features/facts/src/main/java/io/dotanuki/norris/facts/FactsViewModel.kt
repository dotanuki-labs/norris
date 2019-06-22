package io.dotanuki.norris.facts

import io.dotanuki.norris.architecture.StateMachine
import io.dotanuki.norris.architecture.UnsupportedUserInteraction
import io.dotanuki.norris.architecture.UserInteraction
import io.dotanuki.norris.architecture.UserInteraction.OpenedScreen
import io.dotanuki.norris.architecture.UserInteraction.RequestedFreshContent
import io.dotanuki.norris.rest.services.RemoteFactsService

class FactsViewModel(
    private val service: RemoteFactsService,
    private val machine: StateMachine<List<FactPresentation>>
) {

    fun bind() = machine.states()

    fun handle(interaction: UserInteraction) =
        machine.forward(
            interpret(interaction)
        )

    private fun interpret(interaction: UserInteraction) =
        when (interaction) {
            OpenedScreen, RequestedFreshContent -> ::fetchFromRemote
            else -> throw UnsupportedUserInteraction
        }

    private suspend fun fetchFromRemote() =
        with(service) {
            val randomCategory = availableCategories().random().name
            fetchFacts(randomCategory).map { FactPresentation(it) }
        }
}