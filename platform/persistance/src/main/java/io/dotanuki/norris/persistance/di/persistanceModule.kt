package io.dotanuki.norris.persistance.di

import io.dotanuki.norris.domain.services.CategoriesCacheService
import io.dotanuki.norris.domain.services.SearchesHistoryService
import io.dotanuki.norris.persistance.AppPreferencesWrapper
import io.dotanuki.norris.persistance.CachedCategoriesInfrastructure
import io.dotanuki.norris.persistance.SearchHistoryInfrastructure
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

val persistanceModule = Kodein.Module("persistance") {

    bind<SearchesHistoryService>() with provider {

        val wrapper = AppPreferencesWrapper(
            app = instance()
        )

        SearchHistoryInfrastructure(wrapper.preferences)
    }

    bind<CategoriesCacheService>() with singleton {
        CachedCategoriesInfrastructure
    }
}