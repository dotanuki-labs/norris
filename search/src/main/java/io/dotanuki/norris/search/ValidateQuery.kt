package io.dotanuki.norris.search

import io.dotanuki.norris.architecture.UserInteraction

data class ValidateQuery(val query: String) : UserInteraction.Feature()