package com.github.gibbrich.breakingnews.manager

import android.view.View
import androidx.navigation.NavController
import com.github.gibbrich.breakingnews.core.model.Article

interface INavigationManager {
    var navController: NavController?

    fun switchToArticleDetailScreen(articleImage: View, character: Article)
    fun exit()
}

class NavigationManager: INavigationManager {
    override var navController: NavController? = null

    override fun switchToArticleDetailScreen(articleImage: View, character: Article) {

    }

    override fun exit() {
        navController?.popBackStack()
    }
}
