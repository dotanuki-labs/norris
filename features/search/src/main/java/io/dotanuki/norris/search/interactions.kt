package io.dotanuki.norris.search

import io.dotanuki.norris.architecture.CommandTrigger
import io.dotanuki.norris.architecture.StateTransition
import io.dotanuki.norris.architecture.UserInteraction

data class ValidateQuery(val query: String) : UserInteraction, StateTransition.Parameters
data class QueryDefined(val query: String) : UserInteraction, CommandTrigger.Parameters