package io.dotanuki.norris.search.tests

class SearchViewModelTests {

    // @get:Rule val helper = CoroutinesTestHelper()
    //
    // private lateinit var viewModel: SearchViewModel
    //
    // object FakeSearchesHistoryService : SearchesHistoryService {
    //     override suspend fun lastSearches(): List<String> = listOf("Code")
    //
    //     override fun registerNewSearch(term: String) = Unit
    // }
    //
    // object FakeCategoriesCacheService : CategoriesCacheService {
    //     override fun save(categories: List<RelatedCategory.Available>) = Unit
    //
    //     override fun cached(): List<RelatedCategory.Available>? = listOf(
    //         RelatedCategory.Available("dev"),
    //         RelatedCategory.Available("humor")
    //     )
    // }
    //
    // object FakeRemoteFactsService : RemoteFactsService {
    //     override suspend fun availableCategories(): List<RelatedCategory.Available> =
    //         listOf(
    //             RelatedCategory.Available("dev"),
    //             RelatedCategory.Available("humor"),
    //             RelatedCategory.Available("soccer")
    //         )
    //
    //     override suspend fun fetchFacts(searchTerm: String): List<ChuckNorrisFact> = emptyList()
    // }
    //
    // @Before
    // fun `before each test`() {
    //     val fetchCategories = FetchCategories(FakeCategoriesCacheService, FakeRemoteFactsService)
    //     viewModel = SearchViewModel(FakeSearchesHistoryService, fetchCategories)
    // }
    //
    // @Test
    // fun `should display suggestions`() {
    //     runBlocking {
    //         viewModel.run {
    //             bind().test {
    //
    //                 val recommended = listOf("dev", "humor")
    //                 val previousSearch = listOf("Code")
    //
    //                 val initial = SearchScreenState.INITIAL
    //                 val firstFetch = initial.copy(recommendations = Recommendations.Loading)
    //                 val recomendationsLoaded =
    //                     firstFetch.copy(recommendations = Recommendations.Success(recommended))
    //                 val secondFetch =
    //                     recomendationsLoaded.copy(searchHistory = SearchHistory.Loading)
    //                 val historyLoaded =
    //                     secondFetch.copy(searchHistory = SearchHistory.Success(previousSearch))
    //
    //                 val expectedStates = listOf(
    //                     initial,
    //                     firstFetch,
    //                     recomendationsLoaded,
    //                     secondFetch,
    //                     historyLoaded
    //                 )
    //
    //                 handle(SearchInteraction.OpenedScreen)
    //
    //                 val collectedStates = expectedStates.map { expectItem() }
    //                 assertThat(collectedStates).isEqualTo(expectedStates)
    //                 cancelAndIgnoreRemainingEvents()
    //             }
    //         }
    //     }
    // }
    //
    // @Test
    // fun `should validate incoming query`() {
    //     runBlocking {
    //         viewModel.run {
    //             bind().test {
    //                 val initial = SearchScreenState.INITIAL
    //                 val queryValidated = initial.copy(searchQuery = SearchQuery.VALID)
    //                 val expectedStates = listOf(initial, queryValidated)
    //
    //                 handle(SearchInteraction.QueryFieldChanged("Norris"))
    //
    //                 val collectedStates = expectedStates.map { expectItem() }
    //                 assertThat(collectedStates).isEqualTo(expectedStates)
    //                 cancelAndIgnoreRemainingEvents()
    //             }
    //         }
    //     }
    // }
}
