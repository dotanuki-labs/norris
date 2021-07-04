package io.dotanuki.norris.search.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.dotanuki.norris.features.utilties.CoordinatedFlowOfStates
import io.dotanuki.norris.search.data.SearchesDataSource
import io.dotanuki.norris.search.domain.SearchQueryValidation
import io.dotanuki.norris.search.presentation.SearchInteraction.NewQuerySet
import io.dotanuki.norris.search.presentation.SearchInteraction.OpenedScreen
import io.dotanuki.norris.search.presentation.SearchInteraction.SuggestionSelected
import io.dotanuki.norris.search.presentation.SearchScreenState.Content
import io.dotanuki.norris.search.presentation.SearchScreenState.Done
import io.dotanuki.norris.search.presentation.SearchScreenState.Error
import io.dotanuki.norris.search.presentation.SearchScreenState.Idle
import io.dotanuki.norris.search.presentation.SearchScreenState.Loading
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class SearchViewModel(
    private val dataSource: SearchesDataSource
) : ViewModel() {

    private val effects = Channel<SearchInteraction>(Channel.UNLIMITED)
    private val states = CoordinatedFlowOfStates<SearchScreenState>(this, Idle)

    init {
        viewModelScope.launch {
            effects.consumeAsFlow().collect { interaction ->
                when (interaction) {
                    OpenedScreen -> loadPrefilledContent()
                    is NewQuerySet -> validateAndSave(interaction.query)
                    is SuggestionSelected -> save(interaction.query)
                }
            }
        }
    }

    fun bind(): StateFlow<SearchScreenState> = states.expose()

    fun handle(interaction: SearchInteraction) =
        viewModelScope.launch {
            states.update(Loading)
            effects.send(interaction)
        }

    private suspend fun loadPrefilledContent() {
        val newState = try {
            val (suggestions, history) = dataSource.searchOptions()
            Content(suggestions, history)
        } catch (error: Throwable) {
            Error(error)
        }

        states.update(newState)
    }

    private suspend fun validateAndSave(query: String) {
        if (SearchQueryValidation.validate(query)) {
            save(query)
            return
        }

        states.update(Error(SearchHistoryError))
    }

    private suspend fun save(query: String) {
        dataSource.saveNewSearch(query)
        states.update(Done)
    }
}
