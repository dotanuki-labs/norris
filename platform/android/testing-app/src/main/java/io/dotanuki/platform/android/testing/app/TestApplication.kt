package io.dotanuki.platform.android.testing.app

import android.app.Application
import io.dotanuki.platform.jvm.core.rest.ChuckNorrisServiceClient
import io.dotanuki.platform.jvm.testing.rest.FakeChuckNorrisService
import io.dotanuki.platform.jvm.testing.rest.FakeHttpResilience

class TestApplication : Application() {

    val chuckNorrisServiceClient = ChuckNorrisServiceClient(
        FakeChuckNorrisService(), FakeHttpResilience.create()
    )
}
