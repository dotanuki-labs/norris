package io.dotanuki.norris.onboarding.di

import io.dotanuki.norris.architecture.StateMachine
import io.dotanuki.norris.architecture.TaskExecutor
import io.dotanuki.norris.domain.FetchCategories
import io.dotanuki.norris.features.utilties.ConfigChangesAwareStateContainer
import io.dotanuki.norris.features.utilties.KodeinTags
import io.dotanuki.norris.onboarding.OnboardingViewModel
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

val onboardingModule = Kodein.Module("onboarding") {

    bind() from provider {

        val usecase = FetchCategories(
            categoriesCache = instance(),
            remoteFacts = instance()
        )

        val stateContainer = ConfigChangesAwareStateContainer<Unit>(
            host = instance(KodeinTags.hostActivity)
        )

        val stateMachine = StateMachine(
            container = stateContainer,
            executor = TaskExecutor.Concurrent(
                scope = stateContainer.emissionScope,
                dispatcher = instance()
            )
        )

        OnboardingViewModel(usecase, stateMachine)
    }
}