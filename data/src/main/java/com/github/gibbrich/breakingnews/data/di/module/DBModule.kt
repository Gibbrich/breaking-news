package com.github.gibbrich.breakingnews.data.di.module

import android.content.Context
import androidx.room.Room
import com.github.gibbrich.breakingnews.data.db.AppDatabase
import com.github.gibbrich.breakingnews.data.di.DataScope
import dagger.Module
import dagger.Provides

@Module
class DBModule {

    @Provides
    @DataScope
    fun provideDB(
        context: Context
    ): AppDatabase {
        return Room
            .databaseBuilder(context, AppDatabase::class.java, "breacking_news_db")
            .build()
    }
}