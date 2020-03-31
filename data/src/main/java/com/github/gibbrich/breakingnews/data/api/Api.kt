package com.github.gibbrich.breakingnews.data.api

import com.github.gibbrich.breakingnews.data.api.model.NWNewsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {
    @GET("top-headlines")
    suspend fun getTopHeadlines(
        @Query("apiKey") apiKey: String,
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int,
        @Query("country") country: String = "us"
    ): NWNewsResponse
}