package io.dotanuki.norris.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.dotanuki.norris.domain.FetchCategories
import io.dotanuki.norris.domain.SearchQueryValidation
import io.dotanuki.norris.domain.services.SearchesHistoryService
import io.dotanuki.norris.search.SearchInteraction.OpenedScreen
import io.dotanuki.norris.search.SearchInteraction.QueryFieldChanged
import io.dotanuki.norris.search.SearchScreenState.Companion.INITIAL
import io.dotanuki.norris.search.SearchScreenState.Recommendations
import io.dotanuki.norris.search.SearchScreenState.SearchHistory
import io.dotanuki.norris.search.SearchScreenState.SearchQuery
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class SearchViewModel(
    private val searchService: SearchesHistoryService,
    private val fetchCategories: FetchCategories
) : ViewModel() {

    private val states = MutableStateFlow(INITIAL)
    private val interactions = Channel<SearchInteraction>(Channel.UNLIMITED)

    init {
        viewModelScope.launch {
            interactions.consumeAsFlow().collect { interaction ->
                when (interaction) {
                    OpenedScreen -> loadPrefilledOptions()
                    is QueryFieldChanged -> validate(interaction.query)
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
        loadCategoriesNamesAsRecommendations()
        loadSearchHistory()
    }

    private suspend fun loadSearchHistory() {
        val actualState = states.value
        val loadingState = actualState.copy(searchHistory = SearchHistory.Loading)
        states.value = loadingState

        try {
            val searchHistory = searchService.lastSearches()
            val successState = loadingState.copy(searchHistory = SearchHistory.Success(searchHistory))
            states.value = successState
        } catch (error: Throwable) {
            val errorState = loadingState.copy(searchHistory = SearchHistory.Failed(error))
            states.value = errorState
        }
    }

    private suspend fun loadCategoriesNamesAsRecommendations() {
        val actualState = states.value
        val loadingState = actualState.copy(recommendations = Recommendations.Loading)
        states.value = loadingState

        try {
            val categoriesNames = fetchCategories.execute().map { it.name }
            val successState = loadingState.copy(recommendations = Recommendations.Success(categoriesNames))
            states.value = successState
        } catch (error: Throwable) {
            val errorState = loadingState.copy(recommendations = Recommendations.Failed(error))
            states.value = errorState
        }
    }

    private fun validate(query: String) {
        val actualState = states.value
        val validatedQuery = if (SearchQueryValidation.validate(query)) SearchQuery.VALID else SearchQuery.INVALID
        val newState = actualState.copy(searchQuery = validatedQuery)
        states.value = newState
    }
}
