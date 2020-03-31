package com.github.gibbrich.breakingnews.data.di

import com.github.gibbrich.breakingnews.core.di.DI as CoreDI


object DI {
    val dataComponent: DataComponent by lazy {
        DaggerDataComponent
            .builder()
            .coreComponent(CoreDI.coreComponent)
            .build()
    }
}