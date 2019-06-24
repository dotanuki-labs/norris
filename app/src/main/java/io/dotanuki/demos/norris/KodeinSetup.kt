package io.dotanuki.demos.norris

import io.dotanuki.demos.norris.logger.loggerModule
import io.dotanuki.norris.facts.di.factsModule
import io.dotanuki.norris.domain.di.restInfrastructureModule
import org.kodein.di.conf.ConfigurableKodein

object KodeinSetup {

    val kodein by lazy {
        ConfigurableKodein(mutable = true).apply {
            addImport(loggerModule)
            addImport(restInfrastructureModule)
            addImport(factsModule)
        }
    }
}