package io.dotanuki.features.facts.util

import android.content.Intent
import androidx.core.app.AppComponentFactory
import io.dotanuki.features.facts.di.FactsActivityFactory
import io.dotanuki.features.facts.ui.FactsActivity
import io.dotanuki.platform.jvm.testing.mockserver.MockServerUrlFactory

class FactsComponentFactory : AppComponentFactory() {
    override fun instantiateActivityCompat(loader: ClassLoader, className: String, intent: Intent?) =
        with(MockServerUrlFactory) {
            when (loader.loadClass(className)) {
                FactsActivity::class.java -> FactsActivityFactory.create()
                else -> super.instantiateActivityCompat(loader, className, intent)
            }
        }
}
