package com.github.gibbrich.breakingnews.core.repository

import com.github.gibbrich.breakingnews.core.model.Article

interface ArticlesRepository {
    suspend fun getTopHeadLines(page: Int, articlesInPage: Int): List<Article>
}