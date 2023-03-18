package io.dotanuki.platform.android.core.persistance.di

import io.dotanuki.platform.android.core.persistance.AppPreferencesWrapper
import io.dotanuki.platform.android.core.persistance.LocalStorage
import io.dotanuki.platform.android.core.persistance.SearchHistoryInfrastructure
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
