package io.dotanuki.platform.jvm.core.rest.di

import okhttp3.HttpUrl

interface ApiUrlFactory {

    val apiUrl: HttpUrl
}
