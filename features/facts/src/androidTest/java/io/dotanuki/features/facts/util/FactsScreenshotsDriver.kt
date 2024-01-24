package io.dotanuki.features.facts.util

import io.dotanuki.features.facts.R
import io.dotanuki.features.facts.presentation.FactsScreenState
import io.dotanuki.features.facts.ui.FactsView
import io.dotanuki.platform.android.testing.screenshots.ScreenshotDriver

object FactsScreenshotsDriver : ScreenshotDriver<FactsScreeshotsHelperActivity, FactsScreenState> {
    override fun beforeCapturing(target: FactsScreeshotsHelperActivity, state: FactsScreenState) {
        val factsView = target.findViewById<FactsView>(R.id.factsViewRoot)
        listOf(FactsScreenState.Idle, state).forEach { factsView.updateWith(it) }
    }

    override fun imageName(state: FactsScreenState): String =
        "FactsScreenshotTests-${state.javaClass.simpleName}State"
}
