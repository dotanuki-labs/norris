package io.dotanuki.norris.facts

import androidx.appcompat.app.AppCompatActivity
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware

fun AppCompatActivity.selfBind(bindings: Kodein.MainBuilder.() -> Unit = {}) =
    Kodein.lazy {
        val parentContainer = (applicationContext as KodeinAware).kodein
        extend(parentContainer)
        bindings.invoke(this)
    }