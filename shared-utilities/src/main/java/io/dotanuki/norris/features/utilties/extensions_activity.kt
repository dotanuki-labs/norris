package io.dotanuki.norris.features.utilties

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.bind
import org.kodein.di.generic.provider

fun AppCompatActivity.selfBind(bindings: Kodein.MainBuilder.() -> Unit = {}) = Kodein.lazy {

    val parentContainer = (applicationContext as KodeinAware).kodein
    extend(parentContainer)

    bind<FragmentActivity>(tag = KodeinTags.hostActivity) with provider {
        this@selfBind
    }

    bindings.invoke(this)
}

fun AppCompatActivity.toast(message: Int) =
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

fun AppCompatActivity.toast(toaster: String) =
    Toast.makeText(this, toaster, Toast.LENGTH_SHORT).show()