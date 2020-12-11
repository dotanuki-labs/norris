package io.dotanuki.norris.search

data class SearchScreenState(
    val searchQuery: SearchQuery,
    val recommendations: Recommendations,
    val searchHistory: SearchHistory
) {
    sealed class Recommendations {
        object Idle : Recommendations()
        object Loading : Recommendations()
        data class Success(val items: List<String>) : Recommendations()
        data class Failed(val error: Throwable) : Recommendations()
    }

    sealed class SearchHistory {
        object Idle : SearchHistory()
        object Loading : SearchHistory()
        data class Success(val items: List<String>) : SearchHistory()
        data class Failed(val error: Throwable) : SearchHistory()
    }

    enum class SearchQuery {
        NOT_SET,
        INVALID,
        VALID
    }

    companion object {
        val INITIAL = SearchScreenState(SearchQuery.NOT_SET, Recommendations.Idle, SearchHistory.Idle)
    }
}
