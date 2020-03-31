package com.github.gibbrich.breakingnews.data.repository

import com.github.gibbrich.breakingnews.core.model.Article
import com.github.gibbrich.breakingnews.core.repository.ArticlesRepository
import com.github.gibbrich.breakingnews.data.converter.ArticlesConverter
import com.github.gibbrich.breakingnews.data.api.Api
import com.github.gibbrich.breakingnews.data.db.AppDatabase

class ArticlesDataRepository(
    private val api: Api,
    private val db: AppDatabase
): ArticlesRepository {
    companion object {
        /**
         * Typically we want to pass API key in compile time via gradle build script
         * and obfuscate, but for simplicity leave as is
         */
        private const val API_KEY = "3dc15b6483c24af8a664397254824bc5"
    }

    /**
     * todo - handle error case:
     * {"status":"error","code":"maximumResultsReached","message":"You have requested too many results. Developer accounts are limited to a max of 100 results. You are trying to request results 100 to 120. Please upgrade to a paid plan if you need more results."}
     * Http error 426
     */
    override suspend fun getTopHeadLines(page: Int, articlesInPage: Int): List<Article> {
        return api.getTopHeadlines(API_KEY, page, articlesInPage)
            .articles
            .map(ArticlesConverter::fromNetwork)
    }
}