package io.dotanuki.demos.norris.search

import androidx.test.ext.junit.runners.AndroidJUnit4
import io.dotanuki.demos.norris.dsl.Visibility.DISPLAYED
import io.dotanuki.demos.norris.dsl.Visibility.HIDDEN
import io.dotanuki.demos.norris.dsl.shouldBe
import io.dotanuki.demos.norris.fakes.FakeApi
import io.dotanuki.demos.norris.fakes.FakeApi.Mode
import io.dotanuki.demos.norris.fakes.FakeCategoriesCache
import io.dotanuki.demos.norris.fakes.FakePersistance
import io.dotanuki.demos.norris.fakes.FakePersistance.Availability
import io.dotanuki.demos.norris.util.ActivityScenarioLauncher.Companion.scenarioLauncher
import io.dotanuki.demos.norris.util.BindingsOverrider
import io.dotanuki.norris.domain.services.CategoriesCacheService
import io.dotanuki.norris.domain.services.SearchesHistoryService
import io.dotanuki.norris.rest.ChuckNorrisDotIO
import io.dotanuki.norris.search.SearchQueryActivity
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.kodein.di.generic.bind
import org.kodein.di.generic.provider

@RunWith(AndroidJUnit4::class)
class SearchQueryAcceptanceTests {

    private val fakeApi by lazy {
        FakeApi()
    }

    private val fakePersistance by lazy {
        FakePersistance()
    }

    private val fakeCache by lazy {
        FakeCategoriesCache()
    }

    @get:Rule val overrider = BindingsOverrider {
        bind<ChuckNorrisDotIO>(overrides = true) with provider {
            fakeApi
        }

        bind<SearchesHistoryService>(overrides = true) with provider {
            fakePersistance
        }

        bind<CategoriesCacheService>(overrides = true) with provider {
            fakeCache
        }
    }

    @Test fun givenAvailableHistory_andAvailableCategories_shouldDisplaySuggestions() {

        fakeApi.mode = Mode.SUCCESS
        fakePersistance.availability = Availability.AVAILABLE

        scenarioLauncher<SearchQueryActivity>().run {
            onResume {
                val categories = listOf("dev", "humor")
                val history = listOf("conjegate", "morobloco")

                searchQueryChecks {
                    loading shouldBe HIDDEN
                    suggestions shouldDisplay categories
                    pastSearches shouldDisplay history
                }
            }
        }
    }

    @Test fun givenUnavailableHistory_shouldReportError() {

        fakeApi.mode = Mode.SUCCESS
        fakePersistance.availability = Availability.UNAVAILABLE

        scenarioLauncher<SearchQueryActivity>().run {
            onResume {
                searchQueryChecks {
                    loading shouldBe HIDDEN
                    errorOnSuggestions shouldBe DISPLAYED
                }
            }
        }
    }

    @Ignore("Failing after Coroutines upgrade") @Test fun shouldReportInvalidQuery() {
        fakeApi.mode = Mode.ERROR
        fakePersistance.availability = Availability.UNAVAILABLE

        scenarioLauncher<SearchQueryActivity>().run {
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
}