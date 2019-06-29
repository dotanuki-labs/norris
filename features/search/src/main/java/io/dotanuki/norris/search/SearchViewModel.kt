package io.dotanuki.norris.search

import io.dotanuki.norris.architecture.StateMachine
import io.dotanuki.norris.architecture.UnsupportedUserInteraction
import io.dotanuki.norris.architecture.UserInteraction
import io.dotanuki.norris.architecture.UserInteraction.OpenedScreen
import io.dotanuki.norris.domain.ComposeSearchOptions

class SearchViewModel(
    private val searchOptions: ComposeSearchOptions,
    private val machine: StateMachine<SearchPresentation>
) {

    fun bind() = machine.states()

    fun handle(interaction: UserInteraction) =
        interpret(interaction)
            .let { task ->
                machine.forward(task)
            }

    private fun interpret(interaction: UserInteraction) =
        when (interaction) {
            is OpenedScreen -> ::showSearchOptions
            else -> throw UnsupportedUserInteraction
        }

    private suspend fun showSearchOptions() =
        searchOptions
            .execute()
            .let { SearchPresentation(it) }
}