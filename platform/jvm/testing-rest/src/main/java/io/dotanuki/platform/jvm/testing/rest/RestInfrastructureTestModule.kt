package io.dotanuki.platform.jvm.testing.rest

import io.dotanuki.platform.jvm.core.rest.ChuckNorrisService
import io.dotanuki.platform.jvm.core.rest.ChuckNorrisServiceClient
import io.dotanuki.platform.jvm.core.rest.RetrofitBuilder
import okhttp3.mockwebserver.MockWebServer
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.singleton

object RestInfrastructureTestModule {

    operator fun invoke(server: MockWebServer) = DI.Module("rest-infrastructure-test-module") {

        bind(overrides = true) {
            val api = RetrofitBuilder(server.url("/")).create(ChuckNorrisService::class.java)

            singleton {
                ChuckNorrisServiceClient(api)
            }
        }
    }
}
