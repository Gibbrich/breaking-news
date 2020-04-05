package com.github.gibbrich.breakingnews.manager

import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import com.github.gibbrich.breakingnews.R
import com.github.gibbrich.breakingnews.core.model.Article
import com.github.gibbrich.breakingnews.ui.ArticleDetailFragment

interface INavigationManager {
    var navController: NavController?

    fun switchToArticleDetailScreen(articleImage: View, article: Article)
    fun exit()
}

class NavigationManager : INavigationManager {
    override var navController: NavController? = null

    override fun switchToArticleDetailScreen(articleImage: View, article: Article) {
        if (navController?.currentDestination?.id == R.id.articleListFragment) {
            val extras = article.urlToImage?.let {
                FragmentNavigatorExtras(articleImage to it)
            }

            navController?.navigate(
                R.id.action_articleListFragment_to_articleDetailFragment,
                ArticleDetailFragment.getParams(article),
                null,
                extras
            )
        }
    }

    override fun exit() {
        navController?.popBackStack()
    }
}
