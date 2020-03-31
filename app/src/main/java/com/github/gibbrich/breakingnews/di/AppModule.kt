package com.github.gibbrich.breakingnews.di

import com.github.gibbrich.breakingnews.manager.INavigationManager
import com.github.gibbrich.breakingnews.manager.NavigationManager
import dagger.Module
import dagger.Provides

@Module
class AppModule {
    @AppScope
    @Provides
    fun provideNavigationManager(): INavigationManager = NavigationManager()
}