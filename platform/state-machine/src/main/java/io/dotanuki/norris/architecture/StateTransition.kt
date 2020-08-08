package io.dotanuki.norris.architecture

sealed class StateTransition<out T> {

    interface Parameters

    class Unparametrized<T> internal constructor(
        val task: suspend () -> T
    ) : StateTransition<T>()

    class Parametrized<T> internal constructor(
        val task: suspend (Parameters) -> T,
        val parameters: Parameters
    ) : StateTransition<T>()

    companion object {

        operator fun <T> invoke(task: suspend () -> T) = Unparametrized(task)

        operator fun <T> invoke(
            task: suspend (Parameters) -> T,
            data: Parameters
        ) = Parametrized(task, data)
    }
}