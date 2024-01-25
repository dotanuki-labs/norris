package io.dotanuki.platform.jvm.testing.rest

sealed class RestScenario {
    object NotDefined : RestScenario()

    data class Categories(val categories: List<String>) : RestScenario()

    data class Facts(val id: String, val fact: String) : RestScenario()

    data class Error(val error: Throwable) : RestScenario()
}
