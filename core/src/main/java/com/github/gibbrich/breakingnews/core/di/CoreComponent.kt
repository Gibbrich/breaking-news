package com.github.gibbrich.breakingnews.core.di

import android.content.Context
import dagger.Component

@CoreScope
@Component(modules = [
    CoreModule::class
])
interface CoreComponent {
    val context: Context
}