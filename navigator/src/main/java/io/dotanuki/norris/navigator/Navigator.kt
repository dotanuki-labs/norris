package io.dotanuki.norris.navigator

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentActivity

class Navigator(
    private val host: FragmentActivity,
    private val links: Map<Screen, Class<Activity>>
) {

    fun delegateWork(destination: Screen, work: DelegatableWork) {
        val next = Intent(host, find(destination))
        host.startActivityForResult(next, work.tag)
    }

    fun notityWorkDone(payload: Bundle) {
        val data = Intent().apply { putExtras(payload) }
        host.run {
            setResult(Activity.RESULT_OK, data)
            finish()
        }
    }

    private fun find(target: Screen) =
        links[target]
            ?.let { it }
            ?: throw UnsupportedNavigation(target)
}