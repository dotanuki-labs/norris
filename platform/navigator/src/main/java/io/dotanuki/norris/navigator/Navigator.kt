package io.dotanuki.norris.navigator

import android.app.Activity
import android.content.Intent
import androidx.fragment.app.FragmentActivity

class Navigator(
    private val host: FragmentActivity,
    private val links: Map<Screen, Class<out Activity>>
) {

    fun navigateTo(destination: Screen) {
        val next = Intent(host, find(destination))
        host.startActivity(next)
    }

    private fun find(target: Screen) =
        links[target] ?: throw UnsupportedNavigation(target)
}
