package io.dotanuki.features.facts.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.dotanuki.features.facts.data.FactsDataSource
import io.dotanuki.features.facts.di.FactsContext
import io.dotanuki.features.facts.domain.FactsRetrievalError

context (FactsContext)
class FactsViewModel : ViewModel() {
    private val dataSource = FactsDataSource()

    private val stateMachine =
        FactsStateMachine(
            initialState = FactsScreenState.Idle,
            machineScope = viewModelScope,
            stateProcessor = ::showFacts
        )

    fun bind() = stateMachine.observe()

    fun handle(newInteraction: FactsUserInteraction) {
        stateMachine.process(
            interaction = newInteraction,
            temporaryState = FactsScreenState.Loading
        )
    }

    private suspend fun showFacts() =
        try {
            FactsScreenState.Success(fetchFacts())
        } catch (error: Throwable) {
            when (error) {
                is FactsRetrievalError.EmptyTerm -> FactsScreenState.Empty
                else -> FactsScreenState.Failed(error)
            }
        }

    private suspend fun fetchFacts(): FactsPresentation {
        val actualSearch = dataSource.actualQuery()
        val relatedFacts = dataSource.search(actualSearch)
        val presentationRows = relatedFacts.map { FactDisplayRow(it) }
        return FactsPresentation(actualSearch, presentationRows)
    }
}
