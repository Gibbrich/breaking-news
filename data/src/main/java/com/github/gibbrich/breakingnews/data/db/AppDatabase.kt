package com.github.gibbrich.breakingnews.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.github.gibbrich.breakingnews.data.converter.DBConverter
import com.github.gibbrich.breakingnews.data.db.dao.ArticlesDao
import com.github.gibbrich.breakingnews.data.db.entity.DBArticle

@Database(entities = [
    DBArticle::class
], version = 1, exportSchema = false)
@TypeConverters(DBConverter::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun charactersDao(): ArticlesDao
}