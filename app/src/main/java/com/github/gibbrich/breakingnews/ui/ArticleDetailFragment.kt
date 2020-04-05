package com.github.gibbrich.breakingnews.ui

import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.transition.TransitionInflater
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

import com.github.gibbrich.breakingnews.R
import com.github.gibbrich.breakingnews.core.model.Article
import com.github.gibbrich.breakingnews.di.DI
import com.github.gibbrich.breakingnews.manager.INavigationManager
import kotlinx.android.synthetic.main.fragment_article_detail.view.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class ArticleDetailFragment : Fragment() {

    companion object {
        private const val TAG = "ArticleDetailFragment"
        const val ARTICLE_ID = "$TAG.ARTICLE_ID"

        fun getParams(article: Article) = Bundle().apply { putParcelable(ARTICLE_ID, article) }
    }

    @Inject
    lateinit var navigationManager: INavigationManager

    private val article by lazy {
        arguments?.getParcelable<Article>(ARTICLE_ID) ?: throw Exception("No Article passed as argument")
    }

    init {
        DI.appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.fragment_article_detail, container, false)

        val transition = TransitionInflater.from(context)
            .inflateTransition(R.transition.image_shared_element_transition)
        sharedElementEnterTransition = transition

        // Avoid a postponeEnterTransition on orientation change, and postpone only of first creation.
        if (savedInstanceState == null) {
            postponeEnterTransition()
        }

        root.fragment_article_detail_image.transitionName = article.urlToImage

        root.fragment_article_detail_title.text = article.title

        getAuthorFormatted(article.author, article.source)?.let {
            root.fragment_article_detail_author.text = it
        } ?: kotlin.run {
            root.fragment_article_detail_author.visibility = View.GONE
        }

        article.publishedAt?.let {
            root.fragment_article_detail_publish_date.text = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(it)
        } ?: kotlin.run {
            root.fragment_article_detail_publish_date.visibility = View.GONE
        }

        article.content?.let {
            root.fragment_article_detail_content.text = getContectFormatted(it)
        } ?: kotlin.run {
            root.fragment_article_detail_content.visibility = View.GONE
        }
        root.fragment_article_detail_read_more.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(article.url))
            startActivity(browserIntent)
        }

        root.fragment_article_detail_close_button.setOnClickListener {
            navigationManager.exit()
        }

        article.urlToImage?.let {
            Glide.with(this)
                .load(it)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any,
                        target: Target<Drawable>,
                        isFirstResource: Boolean
                    ): Boolean {
                        // The postponeEnterTransition is called on the parent ImagePagerFragment, so the
                        // startPostponedEnterTransition() should also be called on it to get the transition
                        // going in case of a failure.
                        startPostponedEnterTransition()
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable,
                        model: Any,
                        target: Target<Drawable>,
                        dataSource: DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        // The postponeEnterTransition is called on the parent ImagePagerFragment, so the
                        // startPostponedEnterTransition() should also be called on it to get the transition
                        // going when the image is ready.
                        startPostponedEnterTransition()
                        return false
                    }
                })
                .error(R.drawable.ic_broken_image_black_24dp)
                .into(root.fragment_article_detail_image)
        } ?: kotlin.run {
            root.fragment_article_detail_image.setImageDrawable(context!!.getDrawable(R.drawable.ic_broken_image_black_24dp))
        }

        return root
    }

    private fun getContectFormatted(content: String): String =
        content.replace(Regex("\\s(\\w+).\\s\\[.*\\]\$"), "...")

    private fun getAuthorFormatted(author: String?, source: String?): String? = when {
        author.isNullOrBlank() && source.isNullOrBlank() -> null
        author.isNullOrBlank() -> source
        source.isNullOrBlank() -> author
        else -> getString(R.string.fragment_article_detail_author, article.author, article.source)
    }
}
