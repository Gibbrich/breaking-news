package com.github.gibbrich.breakingnews.core.repository

import com.github.gibbrich.breakingnews.core.model.Article

/**
 * Main source of [Article]
 */
interface ArticlesRepository {
    /**
     * All articles, that were fetched from server and cached in memory
     */
    val cachedArticles: List<Article>

    /**
     * Fetches additional page of [Article] from server
     * @param page number of page to display
     * @param articlesInPage max amount of [Article] in this page
     */
    suspend fun getTopHeadLines(page: Int, articlesInPage: Int): List<Article>
}