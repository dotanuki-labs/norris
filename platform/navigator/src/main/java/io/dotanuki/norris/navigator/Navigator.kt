package io.dotanuki.norris.navigator

import android.app.Activity
import android.content.Intent
import androidx.fragment.app.FragmentActivity

class Navigator(
    private val host: FragmentActivity,
    private val links: Map<Screen, Class<Activity>>
) {

    fun navigateTo(destination: Screen) {
        val next = Intent(host, find(destination))
        host.startActivity(next)
    }

    fun requestWork(destination: Screen, work: DelegatableWork) {
        val next = Intent(host, find(destination))
        host.startActivityForResult(next, work.tag)
    }

    fun returnFromWork() {
        host.run {
            setResult(Activity.RESULT_OK)
            finish()
        }
    }

    private fun find(target: Screen) =
        links[target]
            ?.let { it }
            ?: throw UnsupportedNavigation(target)
}