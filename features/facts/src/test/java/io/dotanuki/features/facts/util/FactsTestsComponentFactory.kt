package io.dotanuki.features.facts.util

import android.app.Application
import android.content.Intent
import androidx.core.app.AppComponentFactory
import io.dotanuki.features.facts.di.FactsModule
import io.dotanuki.features.facts.ui.FactsActivity
import io.dotanuki.platform.android.core.persistance.di.PersistanceModule
import io.dotanuki.platform.android.testing.app.TestApplication
import io.dotanuki.platform.jvm.core.rest.ChuckNorrisServiceClient

class FactsTestsComponentFactory : AppComponentFactory() {

    lateinit var app: Application
    lateinit var chuckNorrisServiceClient: ChuckNorrisServiceClient

    override fun instantiateActivityCompat(cl: ClassLoader, className: String, intent: Intent?) =
        when (val activityClass = cl.loadClass(className)) {
            FactsActivity::class.java -> createFactsActivity()
            else -> error("Only FactsActivity is supported by this factory!")
        }

    override fun instantiateApplicationCompat(cl: ClassLoader, className: String): Application =
        super.instantiateApplicationCompat(cl, className).apply {
            app = this
            chuckNorrisServiceClient = (app as TestApplication).chuckNorrisServiceClient
        }

    private fun createFactsActivity(): FactsActivity {
        val localStorage = PersistanceModule(app).localStorage
        val factsModule = FactsModule(localStorage, chuckNorrisServiceClient)
        return FactsActivity(factsModule.vmFactory)
    }
}
