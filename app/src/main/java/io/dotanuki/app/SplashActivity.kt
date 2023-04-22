package io.dotanuki.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import io.dotanuki.platform.android.core.navigator.Navigator
import io.dotanuki.platform.android.core.navigator.Screen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import android.R.anim.fade_in as FadeIn
import android.R.anim.fade_out as FadeOut

class SplashActivity : AppCompatActivity() {

    private val navigator: Navigator = TODO()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        lifecycleScope.launch {
            delay(1000)
            proceedToFacts()
        }
    }

    private fun proceedToFacts() {
        navigator.navigateTo(this, Screen.FactsList)
        overridePendingTransition(FadeIn, FadeOut)
        finish()
    }
}
