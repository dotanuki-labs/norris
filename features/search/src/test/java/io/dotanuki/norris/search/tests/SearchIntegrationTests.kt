package io.dotanuki.norris.search.tests

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import io.dotanuki.norris.persistance.LocalStorage
import io.dotanuki.norris.rest.ChuckNorrisDotIO
import io.dotanuki.norris.search.di.searchModule
import io.dotanuki.norris.search.presentation.SearchScreenState
import io.dotanuki.norris.search.ui.SearchActivity
import io.dotanuki.testing.app.TestApplication
import io.dotanuki.testing.app.activityScenario
import io.dotanuki.testing.app.awaitPendingExecutions
import io.dotanuki.testing.rest.FakeChuckNorrisIO
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.kodein.di.direct
import org.kodein.di.instance
import org.robolectric.annotation.Config
import org.robolectric.annotation.LooperMode

@RunWith(AndroidJUnit4::class)
@Config(application = TestApplication::class)
@LooperMode(LooperMode.Mode.PAUSED)
class SearchIntegrationTests {

    private lateinit var localStorage: LocalStorage

    private val suggestions =
        """
        [
            "career",
            "celebrity",
            "dev"
        ]
        """.trimIndent()

    @Before fun `before each test`() {
        val app = TestApplication.setupWith(searchModule)
        val api = app.di.direct.instance<ChuckNorrisDotIO>() as FakeChuckNorrisIO

        api.run {
            prepare()
            fakeCategories = suggestions
        }

        localStorage = app.di.direct.instance()
    }

    @Test fun `at first lunch, should display only suggestions`() {
        activityScenario<SearchActivity> {
            whenResumed {

                val expectedState = SearchScreenState.Content(
                    suggestions = listOf("career", "celebrity", "dev"),
                    history = emptyList()
                )

                assertThat(it.actualState).isEqualTo(expectedState)
            }
        }
    }

    @Test fun `should proceed saving term chosen from suggestions`() {

        localStorage.registerNewSearch("code")

        activityScenario<SearchActivity> {
            whenResumed { target ->
                target.onChipClicked("dev")

                val savedSearches = runBlocking { localStorage.lastSearches() }

                awaitPendingExecutions()

                val expectedSearches = listOf("code", "dev")
                val expectedState = SearchScreenState.Done

                assertThat(savedSearches).isEqualTo(expectedSearches)
                assertThat(target.actualState).isEqualTo(expectedState)
            }
        }
    }

    @Test fun `should proceed saving new search term`() {

        localStorage.registerNewSearch("code")
        localStorage.registerNewSearch("dev")

        activityScenario<SearchActivity> {
            whenResumed { target ->

                target.onQuerySubmited("kotlin")
                val savedSearches = runBlocking { localStorage.lastSearches() }

                awaitPendingExecutions()

                val expectedSearches = listOf("code", "dev", "kotlin")
                val expectedState = SearchScreenState.Done

                assertThat(savedSearches).isEqualTo(expectedSearches)
                assertThat(target.actualState).isEqualTo(expectedState)
            }
        }
    }
}
