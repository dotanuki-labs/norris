package io.dotanuki.platform.jvm.core.rest

import retrofit2.http.GET
import retrofit2.http.Query

interface ChuckNorrisService {
    @GET("/jokes/categories")
    suspend fun categories(): List<String>

    @GET("/jokes/search")
    suspend fun search(
        @Query("query") query: String,
    ): RawSearch
}
