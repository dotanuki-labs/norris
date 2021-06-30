package io.dotanuki.testing.rest

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer

fun MockWebServer.nextSuccess(payload: String) {
    enqueue(
        MockResponse().setResponseCode(200).setBody(payload)
    )
}
