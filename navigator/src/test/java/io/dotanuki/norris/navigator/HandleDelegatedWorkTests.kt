package io.dotanuki.norris.navigator

import android.app.Activity
import android.content.Intent
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class HandleDelegatedWorkTests {

    @Test fun `should handle delegated work with results`() {

        val work = DefineSearchQuery

        val result = HandleDelegatedWork(
            requestCode = work.tag,
            resultCode = Activity.RESULT_OK,
            data = Intent(),
            flow = work
        )

        assertThat(result).isInstanceOf(PostFlow.WithResults::class.java)
    }

    @Test fun `should handle delegated work with no results`() {

        val work = DefineSearchQuery
        val result = HandleDelegatedWork(
            requestCode = work.tag,
            resultCode = Activity.RESULT_CANCELED,
            data = null,
            flow = work
        )

        val expected = PostFlow.NoResults
        assertThat(result).isEqualTo(expected)
    }

    @Test fun `should report no results for diferent flow tag`() {

        val work = DefineSearchQuery

        val result = HandleDelegatedWork(
            requestCode = 0xCAFE,
            resultCode = Activity.RESULT_OK,
            data = Intent(),
            flow = work
        )

        val expected = PostFlow.NoResults
        assertThat(result).isEqualTo(expected)
    }
}