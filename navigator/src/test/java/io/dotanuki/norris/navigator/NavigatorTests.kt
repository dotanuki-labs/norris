package io.dotanuki.norris.navigator

import android.app.Activity
import android.content.Intent
import androidx.fragment.app.FragmentActivity
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import io.dotanuki.norris.navigator.Screen.FactsList
import io.dotanuki.norris.navigator.Screen.SearchQuery
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class NavigatorTests {

    private lateinit var navigator: Navigator

    private val mockActivity = mock<FragmentActivity>()

    private val links = mapOf<Screen, Class<Activity>>(
        SearchQuery to Activity::class.java
    )

    @Before fun `before each test`() {
        navigator = Navigator(mockActivity, links)
    }

    @Ignore @Test fun `should navigate to supported screen`() {
        navigator.navigateTo(SearchQuery)
        argumentCaptor<Intent>().apply {
            verify(mockActivity).startActivity(capture())
            assertThat(firstValue).isNotNull()
        }
    }

    @Ignore @Test fun `should throw when navigating to unsupported screen`() {

        assertThatThrownBy { navigator.navigateTo(FactsList) }
            .isEqualTo(
                UnsupportedNavigation(FactsList)
            )
    }

    @Ignore @Test fun `should delegate work to supported screen`() {
        navigator.requestWork(SearchQuery, DefineSearchQuery)
        argumentCaptor<Int>().apply {
            verify(mockActivity).startActivityForResult(any(), capture())
            assertThat(firstValue).isEqualTo(DefineSearchQuery.tag)
        }
    }

    @Ignore @Test fun `should return from work with success`() {
        navigator.returnFromWork()
        argumentCaptor<Int>().apply {
            verify(mockActivity).setResult(capture())
            assertThat(firstValue).isEqualTo(Activity.RESULT_OK)
        }
    }
}