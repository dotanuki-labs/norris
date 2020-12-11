package io.dotanuki.demos.norris.search

import io.dotanuki.demos.norris.R
import io.dotanuki.demos.norris.dsl.WriteOnTextInputFieldInteraction

fun searchInteractions(block: SearchInteractions.() -> Unit) =
    SearchInteractions().apply(block)

class SearchInteractions {

    val textInputField = WriteOnTextInputFieldInteraction(R.id.queryTextInput)
}
