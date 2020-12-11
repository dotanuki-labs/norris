package io.dotanuki.norris.navigator

import android.os.Bundle

sealed class PostFlow {
    data class WithResults(val payload: Bundle) : PostFlow()
    object NoResults : PostFlow()
}
