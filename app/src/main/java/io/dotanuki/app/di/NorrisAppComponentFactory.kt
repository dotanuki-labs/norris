package io.dotanuki.app.di

import android.app.Application
import android.content.Intent
import androidx.core.app.AppComponentFactory
import io.dotanuki.features.facts.di.FactsModule
import io.dotanuki.features.facts.ui.FactsActivity
import io.dotanuki.features.search.di.SearchModule
import io.dotanuki.features.search.ui.SearchActivity
import io.dotanuki.platform.android.core.persistance.di.PersistanceModule
import io.dotanuki.platform.jvm.core.rest.ChuckNorrisService
import io.dotanuki.platform.jvm.core.rest.ChuckNorrisServiceClient
import io.dotanuki.platform.jvm.core.rest.HttpResilience
import io.dotanuki.platform.jvm.core.rest.RetrofitBuilder

class NorrisAppComponentFactory : AppComponentFactory() {

    lateinit var app: Application

    private val localStorage by lazy {
        PersistanceModule(app).localStorage
    }

    private val chuckNorrisServiceClient by lazy {
        val httpResilience = HttpResilience.createDefault()
        val service = RetrofitBuilder(ApplicationModule.apiUrl, httpResilience).create(ChuckNorrisService::class.java)
        ChuckNorrisServiceClient(service, httpResilience)
    }


    override fun instantiateActivityCompat(loader: ClassLoader, className: String, intent: Intent?) =
        when (loader.loadClass(className)) {
            FactsActivity::class.java -> createFactsActivity()
            SearchActivity::class.java -> createSearchActivity()
            else -> super.instantiateActivityCompat(loader, className, intent)
        }

    private fun createSearchActivity(): SearchActivity {
        val searchModule = SearchModule(localStorage, chuckNorrisServiceClient)
        return SearchActivity(searchModule.vmFactory)
    }

    override fun instantiateApplicationCompat(cl: ClassLoader, className: String): Application =
        super.instantiateApplicationCompat(cl, className).apply {
            app = this
        }

    private fun createFactsActivity(): FactsActivity {
        val factsModule = FactsModule(localStorage, chuckNorrisServiceClient)
        return FactsActivity(factsModule.vmFactory)
    }
}
