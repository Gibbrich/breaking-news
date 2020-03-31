package com.github.gibbrich.breakingnews.adapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.github.gibbrich.breakingnews.R
import com.github.gibbrich.breakingnews.core.model.Article
import com.github.gibbrich.breakingnews.utils.createCircularProgressDrawable
import kotlinx.android.synthetic.main.articles_list_item.view.*
import kotlinx.android.synthetic.main.item_list_loading.view.*

class ArticlesAdapter(
    private val viewHolderListener: ViewHolderListener,
    private val requestManager: RequestManager,
    private val items: MutableList<Article> = mutableListOf()
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        private const val VIEW_TYPE_ITEM = 0
        private const val VIEW_TYPE_FOOTER = 1
    }

    var footerState: FooterState? = null
        private set

    private var footerViewHolder: FooterViewHolder? = null

    init {
        if (items.isNotEmpty()) {
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val context = parent.context

        when (viewType) {
            VIEW_TYPE_ITEM -> {
                val view = LayoutInflater
                    .from(context)
                    .inflate(R.layout.articles_list_item, parent, false)
                val circularProgressDrawable = createCircularProgressDrawable(context)
                return ArticleViewHolder(
                    view,
                    view.articles_list_item_picture,
                    view.articles_list_item_title,
                    view.articles_list_item_source,
                    circularProgressDrawable
                )
            }

            else -> {
                val view = LayoutInflater
                    .from(context)
                    .inflate(R.layout.item_list_loading, parent, false)
                val footerViewHolder = FooterViewHolder(view, viewHolderListener::onRetry)
                this.footerViewHolder = footerViewHolder
                return footerViewHolder
            }
        }

    }

    private fun shouldShowFooter() = footerState != null

    override fun getItemCount(): Int {
        return if (shouldShowFooter()) items.size + 1 else items.size
    }

    override fun getItemViewType(position: Int): Int =
        if (shouldShowFooter() && position == items.size) {
            VIEW_TYPE_FOOTER
        } else {
            VIEW_TYPE_ITEM
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            VIEW_TYPE_ITEM -> {
                (holder as ArticleViewHolder).bind(
                    viewHolderListener,
                    items[position],
                    requestManager
                )
            }

            VIEW_TYPE_FOOTER -> {
                (holder as FooterViewHolder).bind(footerState)
            }
        }
    }

    fun updateFooterState(state: FooterState?) {
        if (footerState == null) {
            footerState = state
            if (state != null) {
                notifyItemInserted(items.size + 1)
            } else {
                notifyItemChanged(items.size)
            }
        } else {
            footerState = state
            if (state == null) {
                notifyItemRemoved(items.size + 1)
            } else {
                notifyItemChanged(items.size)
            }
        }
    }

    fun addItems(items: List<Article>) {
        if (items.isEmpty())
            return

        val positionStart = this.items.size
        this.items.addAll(items)
        notifyItemRangeInserted(positionStart, items.size)
    }
}

private class ArticleViewHolder(
    view: View,
    val picture: ImageView,
    val titleLabel: TextView,
    val sourceLabel: TextView,
    val circularProgressDrawable: CircularProgressDrawable
) : RecyclerView.ViewHolder(view) {

    /**
     * The binding will load the image into the image view, as well as set its transition name for
     * later.
     */
    fun bind(
        viewHolderListener: ViewHolderListener,
        article: Article,
        requestManager: RequestManager
    ) {
        val loadListener = object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any,
                target: Target<Drawable>,
                isFirstResource: Boolean
            ): Boolean {
                viewHolderListener.onLoadCompleted(model.toString())
                return false
            }

            override fun onResourceReady(
                resource: Drawable,
                model: Any,
                target: Target<Drawable>,
                dataSource: DataSource,
                isFirstResource: Boolean
            ): Boolean {
                viewHolderListener.onLoadCompleted(model.toString())
                return false
            }
        }

        article.urlToImage
            ?.let {
                requestManager
                    .load(article.urlToImage)
                    .listener(loadListener)
                    .placeholder(circularProgressDrawable)
                    .error(R.drawable.ic_broken_image_black_24dp)
                    .into(picture)

                // Set the string value of the image resource as the unique transition name for the view.
                picture.transitionName = it
            }
            ?: kotlin.run {
                picture.setImageDrawable(itemView.context.getDrawable(R.drawable.ic_broken_image_black_24dp))
            }


        itemView.setOnClickListener {
            viewHolderListener.onItemClicked(picture, article)
        }
        titleLabel.text = article.title
        sourceLabel.text = article.source
    }
}

private class FooterViewHolder(
    private val view: View,
    private val onRetry: () -> Unit
) : RecyclerView.ViewHolder(view) {

    init {
        view.btn_retry.setOnClickListener { onRetry() }
    }

    fun bind(footerState: FooterState?) = when (footerState) {
        FooterState.LOADING -> {
            view.iv_status.setImageDrawable(createCircularProgressDrawable(itemView.context))
            view.btn_retry.visibility = View.GONE
        }

        FooterState.ERROR -> {
            view.iv_status.setImageResource(R.drawable.ic_error_outline_black_24dp)
            view.btn_retry.visibility = View.VISIBLE
        }

        null -> Unit
    }
}

enum class FooterState {
    LOADING, ERROR
}

/**
 * A listener that is attached to all ViewHolders to handle image loading events and clicks.
 */
interface ViewHolderListener {

    /**
     * Notifies, whether specific picture was loaded and ready to display.
     * @param model URL of picture, that was loaded
     */
    fun onLoadCompleted(model: String)

    /**
     * Handle specific item click.
     * @param view ImageView, which holds [Character.photoUrl]
     * @param article [Article], which card was clicked
     */
    fun onItemClicked(view: View, article: Article)

    /**
     * Handle error view button "Retry" click
     */
    fun onRetry()
}