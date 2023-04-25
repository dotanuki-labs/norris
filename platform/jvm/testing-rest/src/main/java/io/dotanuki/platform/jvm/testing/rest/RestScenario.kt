package io.dotanuki.platform.jvm.testing.rest

import io.dotanuki.platform.jvm.core.rest.RawSearch

sealed class RestScenario {
    object NotDefined : RestScenario()
    data class Categories(val results: List<String>) : RestScenario()
    data class Facts(val result: RawSearch) : RestScenario()
    data class Error(val error: Throwable) : RestScenario()
}
