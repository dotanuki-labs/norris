package io.dotanuki.platform.android.core.navigator

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity

class Navigator(
    private val links: Map<Screen, Class<out Activity>>
) {

    fun navigateTo(origin: AppCompatActivity, destination: Screen) {
        val next = Intent(origin, find(destination))
        origin.startActivity(next)
    }

    fun toSharingApp(origin: AppCompatActivity, content: String, message: String) {
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
        links[target] ?: throw UnsupportedNavigation(target)
}
