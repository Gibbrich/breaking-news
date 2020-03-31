package com.github.gibbrich.breakingnews.di

object DI {
    lateinit var appComponent: AppComponent
        private set

    fun init(appComponent: AppComponent) {
        DI.appComponent = appComponent
    }
}