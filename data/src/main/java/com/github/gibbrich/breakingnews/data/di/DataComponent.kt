package com.github.gibbrich.breakingnews.data.di

import com.github.gibbrich.breakingnews.core.di.CoreComponent
import com.github.gibbrich.breakingnews.core.repository.ArticlesRepository
import com.github.gibbrich.breakingnews.data.di.module.ApiModule
import com.github.gibbrich.breakingnews.data.di.module.DBModule
import com.github.gibbrich.breakingnews.data.di.module.DataModule
import dagger.Component

@DataScope
@Component(
    modules = [
        ApiModule::class,
        DBModule::class,
        DataModule::class
    ],
    dependencies = [
        CoreComponent::class
    ]
)
interface DataComponent {
    val articlesRepository: ArticlesRepository
}