package io.dotanuki.app.di

import android.app.Application
import android.content.Intent
import androidx.core.app.AppComponentFactory
import io.dotanuki.app.BuildConfig
import io.dotanuki.features.facts.di.FactsViewModelFactory
import io.dotanuki.features.facts.ui.FactsActivity
import io.dotanuki.features.search.di.SearchViewModelFactory
import io.dotanuki.features.search.ui.SearchActivity
import io.dotanuki.platform.android.core.persistance.di.LocalStorageFactory
import io.dotanuki.platform.jvm.core.rest.di.ChuckNorrisServiceClientFactory
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull

class NorrisAppComponentFactory : AppComponentFactory() {

    lateinit var app: Application

    private val apiUrl by lazy {
        val url = when {
            BuildConfig.IS_TEST_MODE -> "https://norris.wiremockapi.cloud/"
            else -> "https://api.chucknorris.io"
        }
        requireNotNull(url.toHttpUrlOrNull())
    }

    private val localStorage by lazy {
        LocalStorageFactory.create(app)
    }

    private val norrisServiceClient by lazy {
        ChuckNorrisServiceClientFactory.create(apiUrl)
    }

    override fun instantiateApplicationCompat(cl: ClassLoader, className: String): Application =
        super.instantiateApplicationCompat(cl, className).apply {
            app = this
        }

    override fun instantiateActivityCompat(loader: ClassLoader, className: String, intent: Intent?) =
        when (loader.loadClass(className)) {
            FactsActivity::class.java -> createFactsActivity()
            SearchActivity::class.java -> createSearchActivity()
            else -> super.instantiateActivityCompat(loader, className, intent)
        }

    private fun createSearchActivity(): SearchActivity =
        SearchActivity(
            SearchViewModelFactory(localStorage, norrisServiceClient)
        )

    private fun createFactsActivity(): FactsActivity =
        FactsActivity(
            FactsViewModelFactory(localStorage, norrisServiceClient)
        )
}
