package io.dotanuki.demos.norris

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import io.dotanuki.norris.app.R
import io.dotanuki.norris.common.android.selfBind
import io.dotanuki.norris.navigator.Navigator
import io.dotanuki.norris.navigator.Screen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.kodein.di.DIAware
import org.kodein.di.instance
import android.R.anim.fade_in as FadeIn
import android.R.anim.fade_out as FadeOut

class SplashActivity : AppCompatActivity(), DIAware {

    override val di by selfBind()

    private val navigator by instance<Navigator>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

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
