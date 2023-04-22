package io.dotanuki.features.search

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import io.dotanuki.features.search.presentation.SearchScreenState
import io.dotanuki.features.search.presentation.SearchScreenState.Content
import io.dotanuki.features.search.presentation.SearchScreenState.Done
import io.dotanuki.features.search.presentation.SearchScreenState.Idle
import io.dotanuki.features.search.presentation.SearchScreenState.Loading
import io.dotanuki.features.search.ui.SearchActivity
import io.dotanuki.features.search.ui.SearchView
import io.dotanuki.platform.android.testing.app.TestApplication
import io.dotanuki.platform.android.testing.app.awaitPendingExecutions
import io.dotanuki.platform.android.testing.app.whenActivityResumed
import io.dotanuki.platform.android.testing.persistance.PersistanceHelper
import io.dotanuki.platform.jvm.testing.rest.RestDataBuilder
import io.dotanuki.platform.jvm.testing.rest.RestInfrastructureRule
import io.dotanuki.platform.jvm.testing.rest.RestInfrastructureTestModule
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(application = TestApplication::class, sdk = [32])
class SearchActivityTests {

    @get:Rule val restInfrastructure = RestInfrastructureRule()

    private val suggestions = RestDataBuilder.suggestionsPayload(
        listOf("career", "celebrity", "dev")
    )

    @Before fun `before each test`() {
        val restTestModule = RestInfrastructureTestModule(restInfrastructure.server)
//        TestApplication.setupWith(searchModule, restTestModule)
        PersistanceHelper.clearStorage()

        restInfrastructure.restScenario(
            status = 200,
            response = suggestions
        )
    }

    @Test fun `at first lunch, should display only suggestions`() {
        whenActivityResumed<SearchActivity> { target ->

            val content = Content(
                suggestions = listOf("career", "celebrity", "dev"),
                history = emptyList()
            )

            val expectedStates = listOf(Idle, Loading, content)
            assertThat(target.receivedStates()).isEqualTo(expectedStates)
        }
    }

    @Test fun `should proceed saving term chosen from suggestions`() {
        PersistanceHelper.registerNewSearch("code")

        whenActivityResumed<SearchActivity> { target ->
            target.onChipClicked("dev")

            awaitPendingExecutions()

            val savedSearches = PersistanceHelper.savedSearches()
            val expectedSearches = listOf("code", "dev")
            assertThat(savedSearches).isEqualTo(expectedSearches)

            val content = Content(
                suggestions = listOf("career", "celebrity", "dev"),
                history = listOf("code")
            )

            val expectedStates = listOf(Idle, Loading, content, Loading, Done)
            assertThat(target.receivedStates()).isEqualTo(expectedStates)
        }
    }

    @Test fun `should proceed saving new search term`() {
        PersistanceHelper.registerNewSearch("code")
        PersistanceHelper.registerNewSearch("dev")

        whenActivityResumed<SearchActivity> { target ->
            target.onNewSearch("kotlin")

            awaitPendingExecutions()

            val savedSearches = PersistanceHelper.savedSearches()
            val expectedSearches = listOf("code", "dev", "kotlin")

            assertThat(savedSearches).isEqualTo(expectedSearches)

            val content = Content(
                suggestions = listOf("career", "celebrity", "dev"),
                history = listOf("code", "dev")
            )

            val expectedStates = listOf(Idle, Loading, content, Loading, Done)
            assertThat(target.receivedStates()).isEqualTo(expectedStates)
        }
    }

    private fun SearchActivity.receivedStates(): List<SearchScreenState> =
        findViewById<SearchView>(R.id.searchScreenRoot).receivedStates

    private fun SearchActivity.onNewSearch(term: String) {
        val rootView = findViewById<SearchView>(R.id.searchScreenRoot)
        rootView.eventsHandler.onNewSearch(term)
    }

    private fun SearchActivity.onChipClicked(item: String) {
        val rootView = findViewById<SearchView>(R.id.searchScreenRoot)
        rootView.eventsHandler.onChipClicked(item)
    }
}
