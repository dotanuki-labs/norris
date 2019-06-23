package io.dotanuki.demos.norris

import android.app.Application
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware

class NorrisApplication : Application(), KodeinAware {

    override val kodein: Kodein = KodeinSetup.kodein
}