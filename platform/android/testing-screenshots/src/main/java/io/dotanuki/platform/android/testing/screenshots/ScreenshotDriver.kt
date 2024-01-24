package io.dotanuki.platform.android.testing.screenshots

import android.app.Activity

interface ScreenshotDriver<A : Activity, State> {
    fun beforeCapturing(target: A, state: State)

    fun imageName(state: State): String
}
