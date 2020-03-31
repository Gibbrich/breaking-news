package com.github.gibbrich.breakingnews.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "articles")
data class DBArticle(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,

    @ColumnInfo(name = "author")
    val author: String,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "description")
    val description: String,

    @ColumnInfo(name = "url")
    val url: String,

    @ColumnInfo(name = "urlToImage")
    val urlToImage: String,

    @ColumnInfo(name = "publishedAt")
    val publishedAt: Date,

    @ColumnInfo(name = "content")
    val content: String
)