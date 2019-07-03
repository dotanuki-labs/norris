package io.dotanuki.demos.norris.search

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.dotanuki.demos.norris.dsl.Visibility.DISPLAYED
import io.dotanuki.demos.norris.dsl.Visibility.HIDDEN
import io.dotanuki.demos.norris.dsl.shouldBe
import io.dotanuki.demos.norris.util.ActivityScenarioLauncher.Companion.scenarioLauncher
import io.dotanuki.demos.norris.util.BindingsOverrider
import io.dotanuki.norris.domain.errors.NetworkingError
import io.dotanuki.norris.domain.rest.ChuckNorrisDotIO
import io.dotanuki.norris.domain.rest.RawCategories
import io.dotanuki.norris.domain.services.SearchesHistoryService
import io.dotanuki.norris.search.SearchQueryActivity
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.kodein.di.generic.bind
import org.kodein.di.generic.provider

@RunWith(AndroidJUnit4::class)
class SearchQueryAcceptanceTests {

    @get:Rule val overrider = BindingsOverrider {
        bind<ChuckNorrisDotIO>(overrides = true) with provider {
            mockChuckNorris
        }

        bind<SearchesHistoryService>(overrides = true) with provider {
            mockPersistance
        }
    }

    private val mockChuckNorris = mock<ChuckNorrisDotIO>()
    private val mockPersistance = mock<SearchesHistoryService>()

    @Test fun givenAvailableHistory_andAvailableCategories_shouldDisplaySuggestions() {

        val categories = listOf("dev", "code")
        val history = listOf("conjegate", "morobloco")

        scenarioLauncher<SearchQueryActivity>().run {

            beforeLaunch {
                runBlocking {
                    whenever(mockPersistance.lastSearches())
                        .thenReturn(history)

                    whenever(mockChuckNorris.categories())
                        .thenReturn(RawCategories(categories))
                }
            }

            onResume {
                searchQueryChecks {
                    loading shouldBe HIDDEN
                    suggestions shouldDisplay categories
                    pastSearches shouldDisplay history
                }
            }
        }
    }

    @Test fun givenUnavailableHistory_shouldReportError() {

        scenarioLauncher<SearchQueryActivity>().run {

            beforeLaunch {
                runBlocking {
                    whenever(mockPersistance.lastSearches()).thenReturn(emptyList())

                    whenever(mockChuckNorris.categories())
                        .thenAnswer { throw NetworkingError.HostUnreachable }
                }
            }

            onResume {
                searchQueryChecks {
                    loading shouldBe HIDDEN
                    errorOnSuggestions shouldBe DISPLAYED
                }
            }
        }
    }

    @Test fun shouldReportInvalidQuery() {
        scenarioLauncher<SearchQueryActivity>().run {

            beforeLaunch {
                runBlocking {
                    whenever(mockPersistance.lastSearches()).thenReturn(emptyList())
                    whenever(mockChuckNorris.categories())
                        .thenReturn(
                            RawCategories(emptyList())
                        )
                }
            }

            onResume {
                searchInteractions {
                    textInputField received "U2"
                }

                searchQueryChecks {
                    validationError shouldBe DISPLAYED
                    confirmQuery()
                    "Cannot proceed with an invalid query" shouldBe DISPLAYED
                }
            }
        }
    }

    @Test fun shouldProceedWithValidQuery() {
        scenarioLauncher<SearchQueryActivity>().run {

            beforeLaunch {
                runBlocking {
                    whenever(mockPersistance.lastSearches())
                        .thenReturn(emptyList())

                    whenever(mockChuckNorris.categories())
                        .thenReturn(RawCategories(emptyList()))
                }
            }

            onResume { scenario ->

                val query = "Code"

                searchInteractions {
                    textInputField received query
                }

                searchQueryChecks {
                    confirmQuery()
                    willForward(scenario, query)
                }
            }
        }
    }
}