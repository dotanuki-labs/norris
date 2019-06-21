package io.dotanuki.norris.rest

import retrofit2.http.GET
import retrofit2.http.Query

internal interface ChuckNorrisRestAPI {

    @GET("/jokes/categories")
    suspend fun categories(): List<RawCategories>

    @GET("/jokes/search")
    suspend fun search(@Query("query") query: String): List<RawSearch>

    companion object {
        const val API_URL = "https://api.chucknorris.io"
    }
}