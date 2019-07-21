package io.dotanuki.norris.architecture

import io.dotanuki.norris.architecture.CommandTrigger.Parametrized
import io.dotanuki.norris.architecture.CommandTrigger.Unparametrized
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow

class CommandsProcessor(
    private val executor: TaskExecutor
) {

    private val broadcaster = ConflatedBroadcastChannel<ViewCommand>()

    fun commands() = broadcaster.asFlow()

    fun process(trigger: CommandTrigger) =
        executor.execute {
            val command = when (trigger) {
                is Unparametrized -> trigger.task.invoke()
                is Parametrized -> with(trigger) { task.invoke(parameters) }
            }
            broadcaster.send(command)
        }
}