package com.github.gibbrich.breakingnews

import android.app.Application
import com.github.gibbrich.breakingnews.di.AppComponent
import com.github.gibbrich.breakingnews.di.DI
import com.github.gibbrich.breakingnews.di.DaggerAppComponent
import com.github.gibbrich.breakingnews.core.di.DI as CoreDI
import com.github.gibbrich.breakingnews.data.di.DI as DataDI

open class App : Application() {

    override fun onCreate() {
        super.onCreate()

        CoreDI.init(this)
        DI.init(provideAppComponent())
    }

    /**
     * Override for implementing UI tests
     */
    open fun provideAppComponent(): AppComponent = DaggerAppComponent
        .builder()
        .dataComponent(DataDI.dataComponent)
        .build()
}