package io.dotanuki.norris.navigator

import android.app.Activity
import android.content.Intent
import androidx.activity.result.ActivityResultCallback
import androidx.fragment.app.FragmentActivity

class Navigator(
    private val host: FragmentActivity,
    private val links: Map<Screen, Class<out Activity>>
) {

    fun navigateTo(destination: Screen) {
        val next = Intent(host, find(destination))
        host.startActivity(next)
    }

    fun navigateForResult(destination: Screen, callback: ActivityResultCallback<String?>) {
        val next = Intent(host, find(destination))
        val contract = DefineSearchQuery(next)
        val launcher = host.registerForActivityResult(contract, callback)
        launcher.launch(Unit)
    }

    fun returnWithResult(query: String) {
        host.run {
            val data = Intent().apply {
                putExtra(DefineSearchQuery.DATA_KEY, query)
            }
            setResult(Activity.RESULT_OK, data)
            finish()
        }
    }

    private fun find(target: Screen) =
        links[target] ?: throw UnsupportedNavigation(target)
}