package io.dotanuki.features.facts.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.dotanuki.features.facts.data.ActualSearchDataSource
import io.dotanuki.features.facts.data.FactsDataSource
import io.dotanuki.features.facts.domain.FactsRetrievalError
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class FactsViewModel(
    private val remoteFacts: FactsDataSource,
    private val actualSearch: ActualSearchDataSource
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
            states.value = FactsScreenState.Loading
            interactions.send(interaction)
        }
    }

    private suspend fun showFacts() {
        states.value = try {
            FactsScreenState.Success(fetchFacts())
        } catch (error: Throwable) {
            when (error) {
                is FactsRetrievalError.EmptyTerm -> FactsScreenState.Empty
                else -> FactsScreenState.Failed(error)
            }
        }
    }

    private suspend fun fetchFacts(): FactsPresentation {
        val actualSearch = actualSearch.actualQuery()
        val relatedFacts = remoteFacts.search(actualSearch)
        val presentationRows = relatedFacts.map { FactDisplayRow(it) }
        return FactsPresentation(actualSearch, presentationRows)
    }
}
