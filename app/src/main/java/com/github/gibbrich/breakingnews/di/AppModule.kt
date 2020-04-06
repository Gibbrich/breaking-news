package com.github.gibbrich.breakingnews.di

import com.github.gibbrich.breakingnews.core.repository.ArticlesRepository
import com.github.gibbrich.breakingnews.manager.INavigationManager
import com.github.gibbrich.breakingnews.manager.NavigationManager
import com.github.gibbrich.breakingnews.ui.ArticleListViewModelFactory
import dagger.Module
import dagger.Provides

@Module
class AppModule {
    @AppScope
    @Provides
    fun provideNavigationManager(): INavigationManager = NavigationManager()

    @AppScope
    @Provides
    fun provideArticleListViewModelFactory(
        articlesRepository: ArticlesRepository
    ): ArticleListViewModelFactory = ArticleListViewModelFactory(articlesRepository)
}