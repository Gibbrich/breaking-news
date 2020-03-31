package com.github.gibbrich.breakingnews.core.di

import android.content.Context
import dagger.Module
import dagger.Provides

@Module
class CoreModule(
    private val context: Context
) {
    @Provides
    @CoreScope
    fun provideContext(): Context = context
}