package io.dotanuki.platform.jvm.testing.mockserver

import io.dotanuki.platform.jvm.core.rest.di.ApiUrlFactory

object MockServerUrlFactory : ApiUrlFactory {

    override val apiUrl = "https://norris.wiremockapi.cloud"
}
