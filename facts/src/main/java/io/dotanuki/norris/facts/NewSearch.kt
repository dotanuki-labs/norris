package io.dotanuki.norris.facts

import io.dotanuki.norris.architecture.UserInteraction

data class NewSearch(val query: String) : UserInteraction.Feature()