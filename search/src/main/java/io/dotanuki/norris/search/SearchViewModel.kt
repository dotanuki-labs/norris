package io.dotanuki.norris.search

import io.dotanuki.norris.architecture.CommandTrigger
import io.dotanuki.norris.architecture.CommandsProcessor
import io.dotanuki.norris.architecture.StateMachine
import io.dotanuki.norris.architecture.StateTransition
import io.dotanuki.norris.architecture.UnsupportedUserInteraction
import io.dotanuki.norris.architecture.UserInteraction
import io.dotanuki.norris.architecture.UserInteraction.OpenedScreen
import io.dotanuki.norris.architecture.ViewCommand
import io.dotanuki.norris.domain.ComposeSearchOptions
import io.dotanuki.norris.domain.ManageSearchQuery
import io.dotanuki.norris.domain.SearchQueryValidation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class SearchViewModel(
    private val optionsComposer: ComposeSearchOptions,
    private val queryManager: ManageSearchQuery,
    private val processor: CommandsProcessor,
    private val machine: StateMachine<SearchPresentation>
) {

    fun bindToStates() = machine.states()

    fun bindToCommands() = processor.commands()

    fun handle(interaction: UserInteraction) =
        when (interaction) {
            is QueryDefined -> {
                processor.process(
                    CommandTrigger(::save, interaction)
                )
            }
            else -> {
                interpret(interaction)
                    .let { transition ->
                        machine.consume(transition)
                    }
            }
        }

    private fun interpret(interaction: UserInteraction) =
        when (interaction) {
            is OpenedScreen -> StateTransition(::showSearchOptions)
            is ValidateQuery -> StateTransition(::validate, interaction)
            else -> throw UnsupportedUserInteraction
        }

    private suspend fun showSearchOptions() =
        optionsComposer
            .execute()
            .let { SearchPresentation.Suggestions(it) }

    private suspend fun validate(parameters: StateTransition.Parameters): SearchPresentation =
        suspendCoroutine { continuation ->
            val interaction = parameters as ValidateQuery
            val valid = SearchQueryValidation.validate(interaction.query)
            continuation.resume(
                SearchPresentation.QueryValidation(valid)
            )
        }

    private suspend fun save(parameters: CommandTrigger.Parameters): ViewCommand =
        with(parameters as QueryDefined) {
            queryManager.save(query)
            ReturnFromSearch
        }
}