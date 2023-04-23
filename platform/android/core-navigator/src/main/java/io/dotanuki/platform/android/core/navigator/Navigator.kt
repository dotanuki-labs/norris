package io.dotanuki.platform.android.core.navigator

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity

class Navigator private constructor(
    private val origin: AppCompatActivity,
    private val mapping: Map<Screen, Class<out Activity>>
) {

    fun navigateTo(destination: Screen) {
        val next = Intent(origin, find(destination))
        origin.startActivity(next)
    }

    fun toSharingApp(content: String, message: String) {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, content)
            type = "text/plain"
        }

        origin.startActivity(
            Intent.createChooser(sendIntent, message)
        )
    }

    private fun find(target: Screen) =
        mapping[target] ?: throw UnsupportedNavigation(target)

    companion object {
        fun AppCompatActivity.retrieveNavigator(): Navigator {
            val mapping = application.let { it as ScreenMappingProvider }.screenMap()
            return Navigator(this, mapping)
        }
    }
}
