package io.dotanuki.norris.onboarding

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import io.dotanuki.norris.common.android.selfBind
import io.dotanuki.norris.common.android.viewBinding
import io.dotanuki.norris.navigator.Navigator
import io.dotanuki.norris.navigator.Screen
import io.dotanuki.norris.onboarding.databinding.ActivityOnboardingBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.kodein.di.DIAware
import org.kodein.di.instance
import android.R.anim.fade_in as FadeIn
import android.R.anim.fade_out as FadeOut

class OnboardingActivity : AppCompatActivity(), DIAware {

    override val di by selfBind()

    private val viewBindings by viewBinding(ActivityOnboardingBinding::inflate)
    private val navigator by instance<Navigator>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBindings.root)

        lifecycleScope.launch {
            delay(1000)
            proceedToFacts()
        }
    }

    private fun proceedToFacts() {
        navigator.navigateTo(Screen.FactsList)
        overridePendingTransition(FadeIn, FadeOut)
        finish()
    }
}
