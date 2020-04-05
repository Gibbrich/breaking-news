package com.github.gibbrich.breakingnews.di

import com.github.gibbrich.breakingnews.MainActivity
import com.github.gibbrich.breakingnews.data.di.DataComponent
import com.github.gibbrich.breakingnews.ui.ArticleDetailFragment
import com.github.gibbrich.breakingnews.ui.ArticleListFragment
import com.github.gibbrich.breakingnews.ui.ArticleListViewModel
import dagger.Component

@AppScope
@Component(
    modules = [
        AppModule::class
    ],
    dependencies = [
        DataComponent::class
    ]
)
interface AppComponent {
    fun inject(entry: MainActivity)
    fun inject(entry: ArticleListViewModel)
    fun inject(entry: ArticleListFragment)
    fun inject(entry: ArticleDetailFragment)
}