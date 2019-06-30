package io.dotanuki.norris.features.navigator.tests

import android.app.Activity
import android.content.Intent
import androidx.fragment.app.FragmentActivity
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import io.dotanuki.norris.features.navigator.DefineSearchQuery
import io.dotanuki.norris.features.navigator.Navigator
import io.dotanuki.norris.features.navigator.Screen
import io.dotanuki.norris.features.navigator.Screen.SearchQuery
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
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

    @Test fun `should delegate work to supported screen`() {
        navigator.delegateWork(SearchQuery, DefineSearchQuery)
        argumentCaptor<Int>().apply {
            verify(mockActivity).startActivityForResult(any(), capture())
            assertThat(firstValue).isEqualTo(DefineSearchQuery.tag)
        }
    }

    @Test fun `should returned from work with success`() {
        val payload = DefineSearchQuery.toPayload("Norris")
        navigator.notityWorkDone(payload)
        argumentCaptor<Intent>().apply {
            verify(mockActivity).setResult(any(), capture())
            assertThat(firstValue.extras).isEqualToComparingFieldByField(payload)
        }
    }
}