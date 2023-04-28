package io.dotanuki.features.search.util

import android.content.Intent
import androidx.core.app.AppComponentFactory
import io.dotanuki.features.search.di.SearchActivityFactory
import io.dotanuki.features.search.ui.SearchActivity
import io.dotanuki.platform.jvm.testing.mockserver.MockServerUrlFactory

class SearchComponentFactory : AppComponentFactory() {

    override fun instantiateActivityCompat(loader: ClassLoader, className: String, intent: Intent?) =
        with(MockServerUrlFactory) {
            when (loader.loadClass(className)) {
                SearchActivity::class.java -> SearchActivityFactory.create()
                else -> super.instantiateActivityCompat(loader, className, intent)
            }
        }
}
