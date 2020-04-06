package com.github.gibbrich.breakingnews.ui

import androidx.lifecycle.*
import com.github.gibbrich.breakingnews.core.model.Article
import com.github.gibbrich.breakingnews.core.repository.ArticlesRepository
import com.github.gibbrich.breakingnews.di.DI
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

class ArticleListViewModel(
    private val articlesRepository: ArticlesRepository
) : ViewModel() {
    companion object {
        private const val ARTICLES_IN_PAGE = 20
    }

    private val articles = mutableListOf<Article>()
    val articlesCached: List<Article> = articles

    private val articlesSource = MutableLiveData<List<Article>>(emptyList())
    val articlesPage: LiveData<List<Article>> = articlesSource

    private val stateSource = MutableLiveData<LoadingState?>()
    val loadingState: LiveData<LoadingState?> = stateSource

    private var pageCount = 1

    fun fetchArticlesPage() {
        if (stateSource.value == LoadingState.LOADING) {
            return
        }

        stateSource.value = LoadingState.LOADING

        viewModelScope.launch {
            val result = try {
                val page = articlesRepository.getTopHeadLines(pageCount, ARTICLES_IN_PAGE)
                stateSource.value = null
                pageCount++

                page
            } catch (e: Exception) {
                e.printStackTrace()
                stateSource.value = LoadingState.ERROR

                emptyList<Article>()
            }

            articles += result

            articlesSource.value = result
            articlesSource.value = emptyList()
        }
    }
}

enum class LoadingState {
    LOADING, ERROR
}

class ArticleListViewModelFactory(private val articlesRepository: ArticlesRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ArticleListViewModel(articlesRepository) as T
    }
}