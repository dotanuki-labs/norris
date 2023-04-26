package io.dotanuki.app.di

import android.app.Application
import android.content.Intent
import androidx.core.app.AppComponentFactory
import io.dotanuki.features.facts.di.FactsActivityFactory
import io.dotanuki.features.facts.ui.FactsActivity
import io.dotanuki.features.search.di.SearchActivityFactory
import io.dotanuki.features.search.ui.SearchActivity
import io.dotanuki.platform.android.core.persistance.PersistanceContextRegistry

class NorrisAppComponentFactory : AppComponentFactory() {

    override fun instantiateApplicationCompat(cl: ClassLoader, className: String): Application =
        super.instantiateApplicationCompat(cl, className).also {
            PersistanceContextRegistry.register(it)
        }

    override fun instantiateActivityCompat(loader: ClassLoader, className: String, intent: Intent?) =
        with(NorrisApiUrlFactory) {
            when (loader.loadClass(className)) {
                FactsActivity::class.java -> FactsActivityFactory.create()
                SearchActivity::class.java -> SearchActivityFactory.create()
                else -> super.instantiateActivityCompat(loader, className, intent)
            }
        }
}
