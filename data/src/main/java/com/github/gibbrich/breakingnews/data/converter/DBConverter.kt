package com.github.gibbrich.breakingnews.data.converter

import androidx.room.TypeConverter
import java.util.*

class DBConverter {
    @TypeConverter
    fun fromLong(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun toLong(value: Date?): Long? {
        return value?.time
    }
}