package io.dotanuki.norris.search.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.dotanuki.norris.search.data.SearchesDataSource
import io.dotanuki.norris.search.domain.SearchQueryValidation
import io.dotanuki.norris.search.presentation.SearchInteraction.OpenedScreen
import io.dotanuki.norris.search.presentation.SearchInteraction.QueryFieldChanged
import io.dotanuki.norris.search.presentation.SearchScreenState.Companion.INITIAL
import io.dotanuki.norris.search.presentation.SearchScreenState.Recommendations
import io.dotanuki.norris.search.presentation.SearchScreenState.SearchHistory
import io.dotanuki.norris.search.presentation.SearchScreenState.SearchQuery
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class SearchViewModel(
    private val dataSource: SearchesDataSource
) : ViewModel() {

    private val states = MutableStateFlow(INITIAL)
    private val interactions = Channel<SearchInteraction>(Channel.UNLIMITED)

    init {
        viewModelScope.launch {
            interactions.consumeAsFlow().collect { interaction ->
                when (interaction) {
                    OpenedScreen -> loadPrefilledOptions()
                    is QueryFieldChanged -> validateAndSave(interaction.query)
                }
            }
        }
    }

    fun bind() = states as StateFlow<SearchScreenState>

    fun handle(interaction: SearchInteraction) =
        viewModelScope.launch {
            interactions.send(interaction)
        }

    private suspend fun loadPrefilledOptions() {
        val (suggestions, history) = dataSource.searchOptions()
        loadCategoriesNamesAsRecommendations(suggestions)
        loadSearchHistory(history)
    }

    private fun loadSearchHistory(history: List<String>) {
        val actualState = states.value
        val loadingState = actualState.copy(searchHistory = SearchHistory.Loading)
        states.value = loadingState

        try {
            val successState = loadingState.copy(searchHistory = SearchHistory.Success(history))
            states.value = successState
        } catch (error: Throwable) {
            val errorState = loadingState.copy(searchHistory = SearchHistory.Failed(error))
            states.value = errorState
        }
    }

    private fun loadCategoriesNamesAsRecommendations(suggestions: List<String>) {
        val actualState = states.value
        val loadingState = actualState.copy(recommendations = Recommendations.Loading)
        states.value = loadingState

        try {
            val successState =
                loadingState.copy(recommendations = Recommendations.Success(suggestions))
            states.value = successState
        } catch (error: Throwable) {
            val errorState = loadingState.copy(recommendations = Recommendations.Failed(error))
            states.value = errorState
        }
    }

    private fun validateAndSave(query: String) {
        val actualState = states.value
        val validatedQuery =
            if (SearchQueryValidation.validate(query)) SearchQuery.VALID else SearchQuery.INVALID
        val newState = actualState.copy(searchQuery = validatedQuery)
        states.value = newState

        if (SearchQueryValidation.validate(query)) {
            viewModelScope.launch {
                dataSource.saveNewSearch(query)
            }
        }
    }
}
