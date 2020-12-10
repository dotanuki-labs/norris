package io.dotanuki.norris.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.dotanuki.norris.domain.FetchCategories
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class OnboardingViewModel(
    private val usecase: FetchCategories
) : ViewModel() {

    private val states = MutableStateFlow<OnboardingScreenState>(OnboardingScreenState.Idle)

    fun bind() = states.asStateFlow()

    fun handleApplicationLaunch() {
        viewModelScope.launch {
            states.value = OnboardingScreenState.Launching
            try {
                usecase.execute()
                states.value = OnboardingScreenState.Success
            } catch (_: Throwable) {
                states.value = OnboardingScreenState.Failed
            }
        }
    }
}