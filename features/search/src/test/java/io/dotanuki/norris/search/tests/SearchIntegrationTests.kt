package io.dotanuki.norris.search.tests

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import io.dotanuki.norris.persistance.LocalStorage
import io.dotanuki.norris.search.di.searchModule
import io.dotanuki.norris.search.presentation.SearchScreenState.Content
import io.dotanuki.norris.search.presentation.SearchScreenState.Done
import io.dotanuki.norris.search.presentation.SearchScreenState.Idle
import io.dotanuki.norris.search.presentation.SearchScreenState.Loading
import io.dotanuki.norris.search.ui.SearchActivity
import io.dotanuki.testing.app.TestApplication
import io.dotanuki.testing.app.activityScenario
import io.dotanuki.testing.app.awaitPendingExecutions
import io.dotanuki.testing.rest.RestDataBuilder
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SearchIntegrationTests {

    private lateinit var localStorage: LocalStorage

    private val suggestions = RestDataBuilder.suggestionsPayload(
        listOf("career", "celebrity", "dev")
    )

    @Before fun `before each test`() {
        val testApplication = TestApplication.setupWith(searchModule).apply {
            api.fakeCategories = suggestions
        }

        localStorage = testApplication.localStorage
    }

    @Test fun `at first lunch, should display only suggestions`() {
        activityScenario<SearchActivity> {
            whenResumed {

                val content = Content(
                    suggestions = listOf("career", "celebrity", "dev"),
                    history = emptyList()
                )

                val expectedStates = listOf(Idle, Loading, content)
                assertThat(it.states).isEqualTo(expectedStates)
            }
        }
    }

    @Test fun `should proceed saving term chosen from suggestions`() {
        localStorage.registerNewSearch("code")

        activityScenario<SearchActivity> {
            whenResumed { target ->

                target.onChipClicked("dev")

                awaitPendingExecutions()

                val savedSearches = runBlocking { localStorage.lastSearches() }
                val expectedSearches = listOf("code", "dev")
                assertThat(savedSearches).isEqualTo(expectedSearches)

                val content = Content(
                    suggestions = listOf("career", "celebrity", "dev"),
                    history = listOf("code")
                )

                val expectedStates = listOf(Idle, Loading, content, Loading, Done)
                assertThat(target.states).isEqualTo(expectedStates)
            }
        }
    }

    @Test fun `should proceed saving new search term`() {
        localStorage.registerNewSearch("code")
        localStorage.registerNewSearch("dev")

        activityScenario<SearchActivity> {
            whenResumed { target ->

                target.onQuerySubmited("kotlin")
                awaitPendingExecutions()

                val savedSearches = runBlocking { localStorage.lastSearches() }
                val expectedSearches = listOf("code", "dev", "kotlin")
                assertThat(savedSearches).isEqualTo(expectedSearches)

                val content = Content(
                    suggestions = listOf("career", "celebrity", "dev"),
                    history = listOf("code", "dev")
                )

                val expectedStates = listOf(Idle, Loading, content, Loading, Done)
                assertThat(target.states).isEqualTo(expectedStates)
            }
        }
    }
}
