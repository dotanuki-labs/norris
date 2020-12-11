package io.dotanuki.demos.norris.fakes

import io.dotanuki.norris.rest.ChuckNorrisDotIO
import io.dotanuki.norris.rest.RawCategories
import io.dotanuki.norris.rest.RawFact
import io.dotanuki.norris.rest.RawSearch
import java.io.IOException

class FakeApi(var mode: Mode = Mode.SUCCESS) : ChuckNorrisDotIO {

    enum class Mode {
        SUCCESS,
        ERROR
    }

    private val timeout by lazy {
        IOException("Connection timeout")
    }

    override suspend fun categories(): RawCategories =
        when (mode) {
            Mode.SUCCESS -> RawCategories(
                listOf(
                    "dev",
                    "humor"
                )
            )
            else -> throw timeout
        }

    override suspend fun search(query: String): RawSearch =
        when (mode) {
            Mode.ERROR -> throw timeout
            else ->
                RawSearch(
                    listOf(
                        RawFact(
                            id = "987654321",
                            categories = emptyList(),
                            url = "https/api.chucknorris.io/987654321",
                            value = "Chuck Norris can divide by zero"
                        )
                    )
                )
        }
}
