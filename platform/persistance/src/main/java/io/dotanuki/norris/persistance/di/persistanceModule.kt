package io.dotanuki.norris.persistance.di

import io.dotanuki.norris.persistance.AppPreferencesWrapper
import io.dotanuki.norris.persistance.LocalStorage
import io.dotanuki.norris.persistance.SearchHistoryInfrastructure
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

val persistanceModule = DI.Module("persistance") {

    bind<LocalStorage> {
        singleton {

            val wrapper = AppPreferencesWrapper(
                app = instance()
            )

            SearchHistoryInfrastructure(wrapper.preferences)
        }
    }
}
