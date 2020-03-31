package com.github.gibbrich.breakingnews.data.di.module

import com.github.gibbrich.breakingnews.core.repository.ArticlesRepository
import com.github.gibbrich.breakingnews.data.api.Api
import com.github.gibbrich.breakingnews.data.db.AppDatabase
import com.github.gibbrich.breakingnews.data.di.DataScope
import com.github.gibbrich.breakingnews.data.repository.ArticlesDataRepository
import dagger.Module
import dagger.Provides

@Module
class DataModule {

    @Provides
    @DataScope
    fun provideArticlesRepository(
        api: Api,
        db: AppDatabase
    ): ArticlesRepository = ArticlesDataRepository(api, db)
}