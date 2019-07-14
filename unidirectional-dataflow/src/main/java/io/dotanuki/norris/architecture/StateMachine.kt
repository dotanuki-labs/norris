package io.dotanuki.norris.architecture

import io.dotanuki.norris.architecture.ViewState.Failed
import io.dotanuki.norris.architecture.ViewState.FirstLaunch
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
        val first = executionStarted()
        moveTo(first)
        val next = perform(execution)
        moveTo(next)
    }

    private suspend fun perform(execution: suspend () -> T) =
        try {
            Success(execution())
        } catch (error: Throwable) {
            Failed(error)
        }

    private fun executionStarted() =
        when (val state = container.current()) {
            is FirstLaunch,
            is Failed -> Loading.FromEmpty
            else -> restoreIfSuccess(state)
        }

    private fun restoreIfSuccess(state: ViewState<T>) =
        when (state) {
            is Success -> Loading.FromPrevious(state.value)
            else -> state
        }

    private suspend fun moveTo(state: ViewState<T>) {
        container.store(state)
    }
}