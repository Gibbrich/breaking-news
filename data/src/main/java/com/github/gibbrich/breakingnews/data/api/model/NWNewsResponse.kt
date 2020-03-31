package com.github.gibbrich.breakingnews.data.api.model

import com.google.gson.annotations.SerializedName

data class NWNewsResponse(
    @SerializedName("articles")
    val articles: List<NWArticle>
)