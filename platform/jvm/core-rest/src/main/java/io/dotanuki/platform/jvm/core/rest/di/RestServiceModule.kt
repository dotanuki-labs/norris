package io.dotanuki.platform.jvm.core.rest.di

import io.dotanuki.platform.jvm.core.rest.ChuckNorrisService
import io.dotanuki.platform.jvm.core.rest.ChuckNorrisServiceClient
import io.dotanuki.platform.jvm.core.rest.HttpResilience
import io.dotanuki.platform.jvm.core.rest.RetrofitBuilder
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

val restServiceModule = DI.Module("rest-service") {

    bind {
        singleton {
            val resilience = HttpResilience.createDefault()
            val retrofit = RetrofitBuilder(apiURL = instance(), resilience)
            val service = retrofit.create(ChuckNorrisService::class.java)
            ChuckNorrisServiceClient(service, resilience)
        }
    }
}
