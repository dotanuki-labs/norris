package io.dotanuki.demos.norris.fakes

import io.dotanuki.demos.norris.fakes.FakePersistance.Availability.AVAILABLE
import io.dotanuki.demos.norris.fakes.FakePersistance.Availability.UNAVAILABLE
import io.dotanuki.norris.domain.errors.NetworkingError
import io.dotanuki.norris.domain.services.SearchesHistoryService

class FakePersistance(var availability: Availability = AVAILABLE) : SearchesHistoryService {

    enum class Availability {
        AVAILABLE,
        UNAVAILABLE
    }

    override suspend fun lastSearches(): List<String> =
        when (availability) {
            AVAILABLE -> listOf("conjegate", "morobloco")
            UNAVAILABLE -> throw NetworkingError.HostUnreachable
        }

    override fun registerNewSearch(term: String) = Unit
}
