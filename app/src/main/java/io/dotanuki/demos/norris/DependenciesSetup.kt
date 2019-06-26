package io.dotanuki.demos.norris

import io.dotanuki.demos.norris.logger.loggerModule
import io.dotanuki.norris.domain.di.restInfrastructureModule
import io.dotanuki.norris.facts.di.factsModule
import org.kodein.di.conf.ConfigurableKodein

object DependenciesSetup {

    val container by lazy {
        ConfigurableKodein(mutable = true).apply {
            addImport(loggerModule)
            addImport(restInfrastructureModule)
            addImport(factsModule)
        }
    }
}