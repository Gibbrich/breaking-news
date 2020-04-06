package com.github.gibbrich.breakingnews.core.repository

import com.github.gibbrich.breakingnews.core.model.Article

interface ArticlesRepository {
    /**
     * Fetches additional page of [Article] from server
     * @param page number of page to display
     * @param articlesInPage max amount of [Article] in this page
     */
    suspend fun getTopHeadLines(page: Int, articlesInPage: Int): List<Article>
}