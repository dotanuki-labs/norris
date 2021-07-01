package io.dotanuki.norris.facts.ui

import android.content.Intent

object SharingHelper {

    fun createChooser(url: String): Intent {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, url)
            type = "text/plain"
        }

        return Intent.createChooser(sendIntent, "Share this Chuck Norris Fact")
    }
}
