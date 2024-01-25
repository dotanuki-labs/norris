package io.dotanuki.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import io.dotanuki.platform.android.core.navigator.Navigator.Companion.retrieveNavigator
import io.dotanuki.platform.android.core.navigator.Screen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import android.R.anim.fade_in as FadeIn
import android.R.anim.fade_out as FadeOut

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        lifecycleScope.launch {
            delay(1000)
            proceedToFacts()
        }
    }

    @Suppress("DEPRECATION")
    private fun proceedToFacts() {
        retrieveNavigator().navigateTo(Screen.FactsList)
        overridePendingTransition(FadeIn, FadeOut)
        finish()
    }
}
