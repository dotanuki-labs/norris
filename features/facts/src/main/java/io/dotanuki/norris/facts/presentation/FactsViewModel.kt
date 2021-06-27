package io.dotanuki.norris.facts.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.dotanuki.norris.facts.data.LatestSearchesDataSource
import io.dotanuki.norris.facts.data.RemoteFactsDataSource
import io.dotanuki.norris.facts.domain.FactsRetrievalError
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class FactsViewModel(
    private val remoteFacts: RemoteFactsDataSource,
    private val latestSearches: LatestSearchesDataSource
) : ViewModel() {

    private val interactions = Channel<FactsUserInteraction>(Channel.UNLIMITED)
    private val states = MutableStateFlow<FactsScreenState>(FactsScreenState.Idle)

    fun bind() = states.asStateFlow()

    init {
        viewModelScope.launch {
            interactions.consumeAsFlow().collect {
                showFacts()
            }
        }
    }

    fun handle(interaction: FactsUserInteraction) {
        viewModelScope.launch {
            interactions.send(interaction)
        }
    }

    private suspend fun showFacts() {
        states.value = FactsScreenState.Loading

        try {
            states.value = FactsScreenState.Success(fetchFacts())
        } catch (error: Throwable) {

            val newState = when (error) {
                is FactsRetrievalError.EmptyTerm -> FactsScreenState.Empty
                else -> FactsScreenState.Failed(error)
            }

            states.value = newState
        }
    }

    private suspend fun fetchFacts(): FactsPresentation {
        val actualSearch = latestSearches.actualQuery()
        val relatedFacts = remoteFacts.search(actualSearch)
        val presentationRows = relatedFacts.map { FactDisplayRow(it) }
        return FactsPresentation(actualSearch, presentationRows)
    }
}
