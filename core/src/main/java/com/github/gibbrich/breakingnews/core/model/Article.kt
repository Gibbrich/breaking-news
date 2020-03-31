package com.github.gibbrich.breakingnews.core.model

import java.util.*

data class Article(
    val author: String?,
    val title: String,
    val description: String?,
    val url: String,
    val urlToImage: String?,
    val publishedAt: Date?,
    val content: String,
    val source: String
)