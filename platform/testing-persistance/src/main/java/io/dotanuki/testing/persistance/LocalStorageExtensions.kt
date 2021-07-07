package io.dotanuki.testing.persistance

import io.dotanuki.norris.persistance.LocalStorage
import org.kodein.di.DIAware
import org.kodein.di.direct
import org.kodein.di.instance

fun DIAware.localStorage(): LocalStorage =
    di.direct.instance()
