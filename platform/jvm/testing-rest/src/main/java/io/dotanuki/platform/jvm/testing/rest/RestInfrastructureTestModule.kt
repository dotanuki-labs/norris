package io.dotanuki.platform.jvm.testing.rest

import okhttp3.mockwebserver.MockWebServer
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.singleton

object RestInfrastructureTestModule {

    operator fun invoke(server: MockWebServer) = DI.Module("rest-infrastructure-test-module") {

        bind(overrides = true) {
            singleton {
                server.wireRestApi()
            }
        }
    }
}
