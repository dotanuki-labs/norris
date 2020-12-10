package io.dotanuki.norris.onboarding

sealed class OnboardingScreenState {
    object Idle : OnboardingScreenState()
    object Launching : OnboardingScreenState()
    object Success : OnboardingScreenState()
    object Failed : OnboardingScreenState()
}
