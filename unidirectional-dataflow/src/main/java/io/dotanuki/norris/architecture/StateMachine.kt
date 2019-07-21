package io.dotanuki.norris.architecture

import io.dotanuki.norris.architecture.StateTransition.Parametrized
import io.dotanuki.norris.architecture.StateTransition.Unparametrized
import io.dotanuki.norris.architecture.ViewState.Failed
import io.dotanuki.norris.architecture.ViewState.FirstLaunch
import io.dotanuki.norris.architecture.ViewState.Loading
import io.dotanuki.norris.architecture.ViewState.Success

class StateMachine<T>(
    private val container: StateContainer<T>,
    private val executor: TaskExecutor
) {

    fun states() = container.observableStates()

    fun consume(execution: StateTransition<T>) =
        executor.execute {
            wrapWithStates(execution)
        }

    private suspend fun wrapWithStates(execution: StateTransition<T>) {
        val first = executionStarted()
        moveTo(first)
        val next = executeWith(execution)
        moveTo(next)
    }

    private suspend fun executeWith(transition: StateTransition<T>): ViewState<T> {
        return try {
            val execution =
                when (transition) {
                    is Unparametrized -> transition.task.invoke()
                    is Parametrized -> with(transition) { this.task.invoke(parameters) }
                }
            Success(execution)
        } catch (error: Throwable) {
            Failed(error)
        }
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