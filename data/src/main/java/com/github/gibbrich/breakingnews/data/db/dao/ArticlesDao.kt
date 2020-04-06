package com.github.gibbrich.breakingnews.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.github.gibbrich.breakingnews.data.db.entity.DBArticle

@Dao
interface ArticlesDao {
    /**
     * Fetch characters chunk. Used for pagination.
     */
    @Query("SELECT * FROM articles LIMIT :limit OFFSET :start")
    suspend fun getArticles(start: Int, limit: Int): List<DBArticle>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(characters: List<DBArticle>)

    @Query("SELECT count(*) FROM articles WHERE id=:id")
    suspend fun isNewsInDB(id: Int): Boolean
}