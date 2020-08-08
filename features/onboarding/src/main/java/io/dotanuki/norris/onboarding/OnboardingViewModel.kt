package io.dotanuki.norris.onboarding

import io.dotanuki.norris.architecture.StateMachine
import io.dotanuki.norris.architecture.StateTransition
import io.dotanuki.norris.architecture.UnsupportedUserInteraction
import io.dotanuki.norris.architecture.UserInteraction
import io.dotanuki.norris.architecture.UserInteraction.OpenedScreen
import io.dotanuki.norris.domain.FetchCategories

class OnboardingViewModel(
    private val usecase: FetchCategories,
    private val machine: StateMachine<Unit>
) {

    fun bind() = machine.states()

    fun handle(interaction: UserInteraction) =
        interpret(interaction).let {
            machine.consume(it)
        }

    private fun interpret(interaction: UserInteraction) =
        when (interaction) {
            is OpenedScreen -> StateTransition(::fetchCategories)
            else -> throw UnsupportedUserInteraction
        }

    private suspend fun fetchCategories() {
        usecase.execute()
    }
}