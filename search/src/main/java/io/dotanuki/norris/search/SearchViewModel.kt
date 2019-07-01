package io.dotanuki.norris.search

import io.dotanuki.norris.architecture.StateMachine
import io.dotanuki.norris.architecture.UnsupportedUserInteraction
import io.dotanuki.norris.architecture.UserInteraction
import io.dotanuki.norris.architecture.UserInteraction.OpenedScreen
import io.dotanuki.norris.domain.ComposeSearchOptions
import io.dotanuki.norris.domain.SearchQueryValidation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class SearchViewModel(
    private val searchOptions: ComposeSearchOptions,
    private val machine: StateMachine<SearchPresentation>
) {

    private var currentQuery = ""

    fun bind() = machine.states()

    fun handle(interaction: UserInteraction) =
        interpret(interaction)
            .let { task ->
                machine.forward(task)
            }

    private fun interpret(interaction: UserInteraction) =
        when (interaction) {
            is OpenedScreen -> ::showSearchOptions
            is ValidateQuery -> {
                currentQuery = (interaction as ValidateQuery).query
                ::validate
            }
            else -> throw UnsupportedUserInteraction
        }

    private suspend fun showSearchOptions() =
        searchOptions
            .execute()
            .let { SearchPresentation.Suggestions(it) }

    private suspend fun validate() =
        suspendCoroutine<SearchPresentation> { continuation ->
            val valid = SearchQueryValidation.validate(currentQuery)
            continuation.resume(SearchPresentation.QueryValidation(valid))
        }
}