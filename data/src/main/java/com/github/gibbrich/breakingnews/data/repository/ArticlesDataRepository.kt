package com.github.gibbrich.breakingnews.data.repository

import com.github.gibbrich.breakingnews.core.model.Article
import com.github.gibbrich.breakingnews.core.repository.ArticlesRepository
import com.github.gibbrich.breakingnews.data.converter.ArticlesConverter
import com.github.gibbrich.breakingnews.data.api.Api
import com.github.gibbrich.breakingnews.data.db.AppDatabase
import retrofit2.HttpException

/**
 * Fetches [Article] from server.
 * Typically we want to store some data in DB, but, as we discussed, for simplicity we decided
 * not persist anything, so everything, what was fetched from server exists only in memory.
 */
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

    override val cachedArticles = mutableListOf<Article>()

    override suspend fun getTopHeadLines(page: Int, articlesInPage: Int): List<Article> {
        return try {
            val articles = api.getTopHeadlines(API_KEY, page, articlesInPage)
                .articles
                .map(ArticlesConverter::fromNetwork)

            cachedArticles += articles

            articles
        } catch (e: Exception) {
            // http 426 error means, that we are out of request limit for free subscription,
            // so just return empty list
            if (e is HttpException && e.code() == 426) {
                return emptyList()
            } else {
                throw e
            }
        }
    }
}