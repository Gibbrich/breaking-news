package com.github.gibbrich.breakingnews.core.di

import android.app.Application

object DI {
    private lateinit var application: Application

    fun init(
        application: Application
    ) {
        DI.application = application
    }

    val coreComponent: CoreComponent by lazy {
        DaggerCoreComponent
            .builder()
            .coreModule(CoreModule(application))
            .build()
    }
}