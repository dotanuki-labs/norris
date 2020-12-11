package io.dotanuki.norris.facts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.dotanuki.norris.domain.FetchFacts
import io.dotanuki.norris.domain.ManageSearchQuery
import io.dotanuki.norris.facts.FactsUserInteraction.DefinedNewSearch
import io.dotanuki.norris.facts.FactsUserInteraction.OpenedScreen
import io.dotanuki.norris.facts.FactsUserInteraction.RequestedFreshContent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class FactsViewModel(
    private val factsFetcher: FetchFacts,
    private val queryManager: ManageSearchQuery,
) : ViewModel() {

    private val interactions = Channel<FactsUserInteraction>(Channel.UNLIMITED)
    private val states = MutableStateFlow<FactsScreenState>(FactsScreenState.Idle)

    fun bind() = states.asStateFlow()

    init {
        viewModelScope.launch {
            interactions.consumeAsFlow().collect { interaction ->
                when (interaction) {
                    OpenedScreen, RequestedFreshContent -> showFacts()
                    is DefinedNewSearch -> {
                        queryManager.save(interaction.query)
                        showFacts()
                    }
                }
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
            states.value = FactsScreenState.Failed(error)
        }
    }

    private suspend fun fetchFacts(): FactsPresentation =
        queryManager.actualQuery().let { query ->
            factsFetcher
                .search(query)
                .map { FactDisplayRow(it) }
                .let { rows -> FactsPresentation(query, rows) }
        }
}
