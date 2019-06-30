package io.dotanuki.norris.features.navigator

import android.app.Activity
import android.content.Intent
import android.os.Bundle

object HandleDelegatedWork {

    operator fun invoke(requestCode: Int, resultCode: Int, data: Intent?, flow: DelegatableWork): PostFlow {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == flow.tag) {
                val extras = data?.extras ?: Bundle.EMPTY
                return PostFlow.WithResults(extras)
            }
        }

        return PostFlow.NoResults
    }
}