package io.dotanuki.norris.features.utilties

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.bind
import org.kodein.di.provider

fun AppCompatActivity.selfBind(bindings: DI.MainBuilder.() -> Unit = {}) = DI.lazy {

    val parentContainer = (applicationContext as DIAware).di
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