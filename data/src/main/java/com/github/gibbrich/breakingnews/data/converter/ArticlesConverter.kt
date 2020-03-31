package com.github.gibbrich.breakingnews.data.converter

import com.github.gibbrich.breakingnews.core.model.Article
import com.github.gibbrich.breakingnews.data.api.model.NWArticle
import java.text.SimpleDateFormat
import java.util.*

object ArticlesConverter {
    private val DEFAULT_DATE_TIME_FORMATTER = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.UK)

    fun fromNetwork(data: NWArticle): Article = Article(
        data.author,
        data.title,
        data.description,
        data.url,
        data.urlToImage,
        DEFAULT_DATE_TIME_FORMATTER.parse(data.publishedAt),
        data.content,
        data.source.name
    )
}