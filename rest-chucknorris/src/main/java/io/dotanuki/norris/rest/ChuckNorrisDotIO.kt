package io.dotanuki.norris.rest

import retrofit2.http.GET
import retrofit2.http.Query

interface ChuckNorrisDotIO {

    @GET("/jokes/categories")
    suspend fun categories(): RawCategories

    @GET("/jokes/search")
    suspend fun search(@Query("query") query: String): RawSearch

    companion object {
        const val API_URL = "https://api.chucknorris.io"
    }
}