package io.dotanuki.norris.architecture

import io.dotanuki.norris.architecture.ViewState.Failed
import io.dotanuki.norris.architecture.ViewState.Loading
import io.dotanuki.norris.architecture.ViewState.Success

class StateMachine<T>(
    private val container: StateContainer<T>,
    private val executor: TaskExecutor
) {

    fun states() = container.observableStates()

    fun forward(task: suspend () -> T) =
        executor.execute {
            wrapWithStates(task)
        }

    private suspend fun wrapWithStates(execution: suspend () -> T) {
        executionStarted()

        val executionCompleted =
            try {
                Success(execution())
            } catch (error: Throwable) {
                Failed(error)
            }

        container.store(executionCompleted)
    }

    private suspend fun executionStarted() =
        container.current()
            ?.let { loadFromPreviousExecution(it) }
            ?: loadFromBeginning()

    private suspend fun loadFromBeginning() {
        container.store(Loading.FromEmpty)
    }

    private suspend fun loadFromPreviousExecution(previous: ViewState<T>) {
        if (previous is Success) {
            container.store(
                Loading.FromPrevious(previous.value)
            )
        }
    }
}