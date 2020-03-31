package com.github.gibbrich.breakingnews.data.api.model

import com.google.gson.annotations.SerializedName

data class NWSource(
    @SerializedName("id")
    val id: String?,

    @SerializedName("name")
    val name: String
)