package io.dotanuki.norris.search.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.dotanuki.norris.search.data.SearchesDataSource
import io.dotanuki.norris.search.domain.SearchQueryValidation
import io.dotanuki.norris.search.presentation.SearchInteraction.NewQuerySet
import io.dotanuki.norris.search.presentation.SearchInteraction.OpenedScreen
import io.dotanuki.norris.search.presentation.SearchInteraction.SuggestionSelected
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class SearchViewModel(
    private val dataSource: SearchesDataSource
) : ViewModel() {

    private val states = MutableStateFlow<SearchScreenState>(SearchScreenState.Idle)
    private val interactions = Channel<SearchInteraction>(Channel.UNLIMITED)

    init {
        viewModelScope.launch {
            interactions.consumeAsFlow().collect { interaction ->
                when (interaction) {
                    OpenedScreen -> loadPrefilledContent()
                    is NewQuerySet -> validateAndSave(interaction.query)
                    is SuggestionSelected -> save(interaction.query)
                }
            }
        }
    }

    fun bind() = states as StateFlow<SearchScreenState>

    fun handle(interaction: SearchInteraction) =
        viewModelScope.launch {
            interactions.send(interaction)
        }

    private suspend fun loadPrefilledContent() {
        states.value = SearchScreenState.Loading

        try {
            val (suggestions, history) = dataSource.searchOptions()
            states.value = SearchScreenState.Content(suggestions, history)
        } catch (error: Throwable) {
            states.value = SearchScreenState.Error(error)
        }
    }

    private suspend fun validateAndSave(query: String) {
        if (SearchQueryValidation.validate(query)) {
            save(query)
            return
        }

        states.value = SearchScreenState.Error(SearchHistoryError)
    }

    private suspend fun save(query: String) {
        dataSource.saveNewSearch(query)
        states.value = SearchScreenState.Done
    }
}
