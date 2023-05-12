package io.dotanuki.features.search.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.dotanuki.features.search.data.SearchesDataSource
import io.dotanuki.features.search.di.SearchContext
import io.dotanuki.features.search.domain.SearchQueryValidation
import io.dotanuki.features.search.presentation.SearchInteraction.NewQuerySet
import io.dotanuki.features.search.presentation.SearchInteraction.OpenedScreen
import io.dotanuki.features.search.presentation.SearchInteraction.SuggestionSelected

context (SearchContext)
class SearchViewModel : ViewModel() {

    private val dataSource = SearchesDataSource()
    private val stateMachine = SearchStateMachine(
        initialState = SearchScreenState.Idle,
        machineScope = viewModelScope,
        stateProcessor = ::reduceToState
    )

    fun bind() = stateMachine.observe()

    fun handle(newInteraction: SearchInteraction) {
        stateMachine.process(
            interaction = newInteraction,
            temporaryState = SearchScreenState.Loading
        )
    }

    private suspend fun reduceToState(incoming: SearchInteraction) =
        when (incoming) {
            OpenedScreen -> loadPrefilledContent()
            is NewQuerySet -> validateAndSave(incoming.query)
            is SuggestionSelected -> save(incoming.query)
        }

    private suspend fun loadPrefilledContent() =
        try {
            val (suggestions, history) = dataSource.searchOptions()
            SearchScreenState.Success(suggestions, history)
        } catch (error: Throwable) {
            SearchScreenState.Failed(error)
        }

    private fun validateAndSave(query: String): SearchScreenState =
        when {
            SearchQueryValidation.validate(query) -> save(query)
            else -> SearchScreenState.Failed(SearchHistoryError)
        }

    private fun save(query: String): SearchScreenState {
        dataSource.saveNewSearch(query)
        return SearchScreenState.Done
    }
}
