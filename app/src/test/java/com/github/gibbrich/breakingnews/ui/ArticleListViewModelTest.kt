package com.github.gibbrich.breakingnews.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.github.gibbrich.breakingnews.core.model.Article
import com.github.gibbrich.breakingnews.core.repository.ArticlesRepository
import com.github.gibbrich.breakingnews.utils.MainCoroutineScopeRule
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import java.lang.IllegalStateException
import java.lang.RuntimeException
import java.util.*
import javax.inject.Inject

class ArticleListViewModelTest {

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @Rule
    @JvmField
    val coroutineScope =  MainCoroutineScopeRule()

    lateinit var viewModel: ArticleListViewModel

    @Mock
    lateinit var articlesRepository: ArticlesRepository

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        viewModel = ArticleListViewModel(articlesRepository)
    }

    @Test
    fun `viewModel charactersCached returns empty list on init`() = runBlocking {
        whenever(articlesRepository.getTopHeadLines(any(), any())).then { emptyList<Article>() }
        assertTrue(viewModel.articlesCached.isNullOrEmpty())
    }

    @Test
    fun `viewModel loadingState start LOADING end null if articles fetch successful`() = runBlocking {
        val list = listOf(Article(
            "Author",
            "Title",
            "Description",
            "Url",
            "UrlToImage",
            Date(),
            "Content",
            "Source"
        ))
        whenever(articlesRepository.getTopHeadLines(any(), any())).then { list }

        var emitCount = 0

        val observer = Observer<LoadingState?> {
            emitCount++

            when (emitCount) {
                1 -> assertTrue(it == LoadingState.LOADING)
                2 -> assertNull(it)
            }
        }

        viewModel.loadingState.observeForever(observer)

        viewModel.fetchArticlesPage()

        assertEquals(2, emitCount)
    }

    @Test
    fun `viewModel loadingState start LOADING end ERROR if articles fetch fails`() = runBlocking {
        whenever(articlesRepository.getTopHeadLines(any(), any())).thenThrow(RuntimeException::class.java)

        var emitCount = 0

        val observer = Observer<LoadingState?> {
            emitCount++

            when (emitCount) {
                1 -> assertTrue(it == LoadingState.LOADING)
                2 -> assertTrue(it == LoadingState.ERROR)
            }
        }

        viewModel.loadingState.observeForever(observer)

        viewModel.fetchArticlesPage()

        assertEquals(2, emitCount)
    }
}