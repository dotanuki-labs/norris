package io.dotanuki.testing.rest

import io.dotanuki.norris.rest.ChuckNorrisDotIO
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.singleton

val testRestInfrastructureModule = DI.Module("rest-infrastructure-fake") {

    bind<ChuckNorrisDotIO> {
        singleton {
            FakeChuckNorrisIO()
        }
    }
}
