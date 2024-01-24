package io.dotanuki.features.search.util

import io.dotanuki.features.search.R
import io.dotanuki.features.search.presentation.SearchScreenState
import io.dotanuki.features.search.ui.SearchView
import io.dotanuki.platform.android.testing.screenshots.ScreenshotDriver

object SearchScreenshotsDriver : ScreenshotDriver<SearchScreenshotsHelperActivity, SearchScreenState> {
    override fun beforeCapturing(target: SearchScreenshotsHelperActivity, state: SearchScreenState) {
        val searchView = target.findViewById<SearchView>(R.id.searchScreenRoot)
        listOf(SearchScreenState.Idle, state).forEach { searchView.updateWith(it) }
    }

    override fun imageName(state: SearchScreenState): String =
        "SearchScreenshotTests-${state.javaClass.simpleName}State"
}
