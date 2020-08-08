package io.dotanuki.norris.architecture

sealed class CommandTrigger {

    interface Parameters

    class Unparametrized(val task: suspend () -> ViewCommand) : CommandTrigger()
    class Parametrized(val task: suspend (Parameters) -> ViewCommand, val parameters: Parameters) : CommandTrigger()

    companion object {
        operator fun invoke(task: suspend () -> ViewCommand) = Unparametrized(task)
        operator fun invoke(task: suspend (Parameters) -> ViewCommand, data: Parameters) = Parametrized(task, data)
    }
}