package io.dotanuki.platform.android.core.helpers

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import io.dotanuki.platform.jvm.core.kodein.KodeinTags
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
