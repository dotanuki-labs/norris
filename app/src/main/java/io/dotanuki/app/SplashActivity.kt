package io.dotanuki.app

import android.os.Bundle
import android.R.anim.fade_in as FadeIn
import android.R.anim.fade_out as FadeOut
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import io.dotanuki.platform.android.core.helpers.selfBind
import io.dotanuki.platform.android.core.navigator.Navigator
import io.dotanuki.platform.android.core.navigator.Screen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.kodein.di.DIAware
import org.kodein.di.instance

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
