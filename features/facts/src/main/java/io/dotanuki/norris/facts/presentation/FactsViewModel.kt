package io.dotanuki.norris.facts.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.dotanuki.norris.facts.data.ActualSearchDataSource
import io.dotanuki.norris.facts.data.FactsDataSource
import io.dotanuki.norris.facts.domain.FactsRetrievalError
import io.dotanuki.norris.facts.presentation.FactsScreenState.Empty
import io.dotanuki.norris.facts.presentation.FactsScreenState.Failed
import io.dotanuki.norris.facts.presentation.FactsScreenState.Idle
import io.dotanuki.norris.facts.presentation.FactsScreenState.Loading
import io.dotanuki.norris.facts.presentation.FactsScreenState.Success
import io.dotanuki.norris.features.utilties.CoordinatedFlowOfStates
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class FactsViewModel(
    private val remoteFacts: FactsDataSource,
    private val actualSearch: ActualSearchDataSource
) : ViewModel() {

    private val effects = Channel<FactsUserInteraction>(Channel.UNLIMITED)
    private val states = CoordinatedFlowOfStates<FactsScreenState>(this, Idle)

    init {
        viewModelScope.launch {
            effects.consumeAsFlow().collect {
                showFacts()
            }
        }
    }

    fun bind(): StateFlow<FactsScreenState> = states.expose()

    fun handle(interaction: FactsUserInteraction) {
        viewModelScope.launch {
            states.update(Loading)
            effects.send(interaction)
        }
    }

    private suspend fun showFacts() {
        val newState = try {
            Success(fetchFacts())
        } catch (error: Throwable) {
            when (error) {
                is FactsRetrievalError.EmptyTerm -> Empty
                else -> Failed(error)
            }
        }

        states.update(newState)
    }

    private suspend fun fetchFacts(): FactsPresentation {
        val actualSearch = actualSearch.actualQuery()
        val relatedFacts = remoteFacts.search(actualSearch)
        val presentationRows = relatedFacts.map { FactDisplayRow(it) }
        return FactsPresentation(actualSearch, presentationRows)
    }
}
