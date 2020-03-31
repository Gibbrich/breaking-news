package com.github.gibbrich.breakingnews.utils

import android.content.Context
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.swiperefreshlayout.widget.CircularProgressDrawable

fun Context.getColorCompat(@ColorRes id: Int) = ContextCompat.getColor(this, id)

fun createCircularProgressDrawable(context: Context) = CircularProgressDrawable(context).apply {
    strokeWidth = 5f
    centerRadius = 30f
    start()
}