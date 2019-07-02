package io.dotanuki.norris.domain.di

import io.dotanuki.norris.networking.RetrofitBuilder
import io.dotanuki.norris.domain.rest.ChuckNorrisDotIO
import io.dotanuki.norris.domain.rest.FactsInfrastructure
import io.dotanuki.norris.domain.services.RemoteFactsService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

val restInfrastructureModule = Kodein.Module("rest-infrastructure") {

    bind() from singleton {

        val logger = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val okHttp = OkHttpClient.Builder()
            .addInterceptor(logger)
            .build()

        val retrofit = RetrofitBuilder(
            apiURL = ChuckNorrisDotIO.API_URL,
            httpClient = okHttp
        )

        retrofit.create(ChuckNorrisDotIO::class.java)
    }

    bind<RemoteFactsService>() with provider {
        FactsInfrastructure(
            rest = instance()
        )
    }
}