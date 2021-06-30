package io.dotanuki.testing.rest

import io.dotanuki.norris.networking.errors.RemoteServiceIntegrationError
import io.dotanuki.norris.rest.ChuckNorrisDotIO
import io.dotanuki.norris.rest.RawCategories
import io.dotanuki.norris.rest.RawSearch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class FakeChuckNorrisIO : ChuckNorrisDotIO {

    private val jsonSerializer by lazy {
        Json {
            isLenient = true
            ignoreUnknownKeys = true
            allowSpecialFloatingPointValues = true
        }
    }

    var errorMode: Boolean = false
    var fakeCategories: String = ""
    var fakeSearch: String = ""

    fun prepare() {
        errorMode = false
        fakeCategories = ""
        fakeSearch = ""
    }

    override suspend fun categories(): RawCategories =
        if (errorMode) throw RemoteServiceIntegrationError.RemoteSystem
        else jsonSerializer.decodeFromString(fakeCategories)

    override suspend fun search(query: String): RawSearch =
        if (errorMode) throw RemoteServiceIntegrationError.RemoteSystem
        else jsonSerializer.decodeFromString(fakeSearch)
}
