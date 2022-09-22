package io.dotanuki.app

import android.app.Application
import org.kodein.di.DIAware

class NorrisApplication : Application(), DIAware {

    override val di = DependenciesSetup(this).container
}
