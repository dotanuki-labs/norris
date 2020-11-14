package io.dotanuki.norris.onboarding

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import io.dotanuki.logger.Logger
import io.dotanuki.norris.architecture.UserInteraction
import io.dotanuki.norris.architecture.ViewState
import io.dotanuki.norris.architecture.ViewState.Failed
import io.dotanuki.norris.architecture.ViewState.FirstLaunch
import io.dotanuki.norris.architecture.ViewState.Success
import io.dotanuki.norris.features.utilties.selfBind
import io.dotanuki.norris.features.utilties.viewBinding
import io.dotanuki.norris.navigator.Navigator
import io.dotanuki.norris.navigator.Screen
import io.dotanuki.norris.onboarding.databinding.ActivityOnboardingBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.kodein.di.DIAware
import org.kodein.di.instance
import android.R.anim.fade_in as FadeIn
import android.R.anim.fade_out as FadeOut

class OnboardingActivity : AppCompatActivity(), DIAware {

    override val di by selfBind()

    private val viewBindings by viewBinding(ActivityOnboardingBinding::inflate)
    private val logger by instance<Logger>()
    private val navigator by instance<Navigator>()
    private val viewModel by instance<OnboardingViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBindings.root)

        lifecycleScope.launch {
            delay(1000)
            viewModel.bind().collect { render(it) }
        }
    }

    private fun render(state: ViewState<Unit>) {
        when (state) {
            FirstLaunch -> launch()
            is Success -> {
                logger.i("Success -> Categories fetched, proceding to facts")
                proceedToFacts()
            }
            is Failed -> {
                logger.e("Error -> $state")
                proceedToFacts()
            }
        }
    }

    private fun launch() {
        viewModel.handle(UserInteraction.OpenedScreen)
    }

    private fun proceedToFacts() {
        navigator.navigateTo(Screen.FactsList)
        overridePendingTransition(FadeIn, FadeOut)
        finish()
    }
}