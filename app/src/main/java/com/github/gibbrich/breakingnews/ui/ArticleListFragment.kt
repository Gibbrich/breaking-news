package com.github.gibbrich.breakingnews.ui

import android.os.Bundle
import android.transition.TransitionInflater
import android.transition.TransitionSet
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.github.gibbrich.breakingnews.R
import com.github.gibbrich.breakingnews.adapter.ArticlesAdapter
import com.github.gibbrich.breakingnews.adapter.FooterState
import com.github.gibbrich.breakingnews.adapter.ViewHolderListener
import com.github.gibbrich.breakingnews.core.model.Article
import com.github.gibbrich.breakingnews.di.DI
import com.github.gibbrich.breakingnews.manager.INavigationManager
import kotlinx.android.synthetic.main.fragment_article_list.*
import javax.inject.Inject

class ArticleListFragment : Fragment() {
    @Inject
    lateinit var navigationManager: INavigationManager

    @Inject
    lateinit var viewModelFactory: ArticleListViewModelFactory

    private val viewModel: ArticleListViewModel by viewModels { viewModelFactory }
    private var adapter: ArticlesAdapter? = null
    private var articleTransitionUrl: String? = null

    private val viewHolderListenerDelegate = object : ViewHolderListener {
        override fun onLoadCompleted(model: String) {
            // Call startPostponedEnterTransition only when the 'selected' image loading is completed.
            if (articleTransitionUrl == model) {
                startPostponedEnterTransition()
            }
        }

        override fun onItemClicked(view: View, article: Article) {
            // Update the position.
            articleTransitionUrl = article.urlToImage

            // Exclude the clicked card from the exit transition (e.g. the card will disappear immediately
            // instead of fading out with the rest to prevent an overlapping animation of fade and move).
            (exitTransition as TransitionSet).excludeTarget(view, true)

            navigationManager.switchToArticleDetailScreen(view, article)
        }

        override fun onRetry() {
            viewModel.fetchArticlesPage()
        }
    }

    init {
        DI.appComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.fetchArticlesPage()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.fragment_article_list, container, false)

        exitTransition =
            TransitionInflater.from(context).inflateTransition(R.transition.grid_exit_transition)

        if (articleTransitionUrl != null) {
            postponeEnterTransition()
        }

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.articlesPage.observe(viewLifecycleOwner, Observer(::handleCharacters))
        viewModel.loadingState.observe(viewLifecycleOwner, Observer(::handleLoadingState))

        val layoutManager = LinearLayoutManager(activity)
        fragment_article_list_recycler.layoutManager = layoutManager

        if (adapter == null) {
            // on fragment recreation we don't need subscription, so we just repopulate
            // cached data
            adapter = ArticlesAdapter(
                viewHolderListenerDelegate,
                Glide.with(this),
                viewModel.articlesCached.toMutableList() // important to perform deep copy to avoid double data, as viewModel.articlesCached populate during work
            )
        }
        fragment_article_list_recycler.adapter = adapter
        setRecyclerViewScrollListener(fragment_article_list_recycler, layoutManager, adapter!!)
    }

    private fun handleLoadingState(state: LoadingState?) {
        val footerState = when (state) {
            LoadingState.LOADING -> FooterState.LOADING
            LoadingState.ERROR -> FooterState.ERROR
            null -> null
        }
        adapter?.updateFooterState(footerState)
    }

    private fun handleCharacters(characters: List<Article>) {
        adapter?.addItems(characters)
    }

    private fun setRecyclerViewScrollListener(
        recyclerView: RecyclerView,
        layoutManager: LinearLayoutManager,
        adapter: ArticlesAdapter
    ) = recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
            val shouldFetchCharacters =
                adapter.footerState == null && layoutManager.itemCount == lastVisibleItemPosition + 1
            if (shouldFetchCharacters) {
                viewModel.fetchArticlesPage()
            }
        }
    })
}
