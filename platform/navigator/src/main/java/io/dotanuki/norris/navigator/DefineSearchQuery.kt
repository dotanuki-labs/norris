package io.dotanuki.norris.navigator

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContract

class DefineSearchQuery(private val implicitIntent: Intent) : ActivityResultContract<Unit, String?>() {

    override fun createIntent(context: Context, input: Unit): Intent = implicitIntent

    override fun parseResult(resultCode: Int, intent: Intent?): String? =
        when (resultCode) {
            Activity.RESULT_OK -> extractData(intent)
            else -> null
        }

    private fun extractData(intent: Intent?): String? {
        val extras = intent?.extras ?: Bundle.EMPTY
        return extras.getString(DATA_KEY)
    }

    companion object {
        const val DATA_KEY = "search.query"
    }
}