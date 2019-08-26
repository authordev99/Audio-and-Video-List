package com.lomotif.android.utils

import android.content.Context
import android.media.Image
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.ObservableArrayList
import com.lomotif.android.model.Video
import com.bumptech.glide.Glide
import com.lomotif.android.Interface.ClickHandler
import com.lomotif.android.MediaQuery
import com.lomotif.android.R
import com.lomotif.android.model.Hit
import com.lomotif.android.model.ImageGallery
import java.io.File
import kotlin.random.Random


open class RecyclerViewAdapter() :
    androidx.recyclerview.widget.RecyclerView.Adapter<RecyclerViewAdapter.BaseViewHolder<*>>() {


    lateinit var context: Context
    private lateinit var listMedia: List<Any>
    var clickHandler: ClickHandler<Any>? = null

    companion object {
        const val ITEM_IMAGE = 1
        const val ITEM_VIDEO_AUDIO = 2
    }


    constructor(
        context: Context,
        listMedia: ObservableArrayList<Any>,
        clickHandler: ClickHandler<Any>?
    ) : this() {
        this.clickHandler = clickHandler
        this.context = context
        this.listMedia = listMedia
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val inflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            ITEM_IMAGE -> ViewHolderImage(inflater.inflate(R.layout.list_item_image, parent,false))
            ITEM_VIDEO_AUDIO -> ViewHolderMedia(inflater.inflate(R.layout.list_item_media, parent, false))
            else -> ViewHolderMedia(inflater.inflate(R.layout.list_item_media, parent, false))
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        val viewType = holder.itemViewType
        val element = listMedia[position]
        when (viewType) {
            ITEM_IMAGE -> {
                val viewHolderImage = holder as ViewHolderImage
                viewHolderImage.bind(element, position)
            }
            ITEM_VIDEO_AUDIO -> {
                val viewHolderMedia = holder as ViewHolderMedia
                viewHolderMedia.bind(element, position)
            }
        }

    }

    override fun getItemCount(): Int = listMedia.size

    override fun getItemViewType(position: Int): Int {
        return when {
            listMedia[position] is Hit -> ITEM_IMAGE
            listMedia[position] is Video -> ITEM_VIDEO_AUDIO
            else -> -1
        }

    }

    inner class ViewHolderMedia(itemView: View) : BaseViewHolder<Any>(itemView) {
        private val tvTitle = itemView.findViewById<TextView>(R.id.tvTitle)
        private val tvDuration = itemView.findViewById<TextView>(R.id.tvDuration)
        private val ivMediaType = itemView.findViewById<ImageView>(R.id.ivMediaType)
        private val ivThumbnail = itemView.findViewById<ImageView>(R.id.ivThumbnail)
        private val flMedia = itemView.findViewById<FrameLayout>(R.id.flMedia)

        override fun bind(item: Any, position: Int) {
            val media = item as Video
            MediaQuery(context).videoFormatSupport.forEach {
                if (media.data.contains(it)) {
                    ivMediaType.setImageDrawable(
                        ResourcesCompat.getDrawable(
                            context.resources,
                            R.drawable.ic_video,
                            null
                        )
                    )
                }
            }

            tvTitle.text = media.display_name
            tvDuration.text = MediaQuery(context).convertTime(media.duration.toLong())
            val uri = Uri.fromFile(File(media.data))
            Glide.with(context).load(uri).thumbnail(0.1f).into(ivThumbnail)

            flMedia.setOnClickListener {
                clickHandler?.onClick(media, position)
            }

        }
    }

    inner class ViewHolderImage(itemView: View) : BaseViewHolder<Any>(itemView) {

        private val ivHits = itemView.findViewById<ImageView>(R.id.ivHits)
        private val cvHits = itemView.findViewById<CardView>(R.id.cvHits)


        override fun bind(item: Any, position: Int) {
            val media = item as Hit

            ivHits.layoutParams.height = getRandomIntInRange(1000,75)

            Glide.with(context)
                .load(media.previewURL)
                .asBitmap()
                .into(ivHits)
            // Set a click listener for card view
            ivHits.setOnClickListener {
                clickHandler?.onClick(media,position)
            }


        }
    }

    abstract class BaseViewHolder<T>(itemView: View) :
        androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        abstract fun bind(item: T, position: Int)
    }

    private fun getRandomIntInRange(max: Int, min: Int): Int {
        return Random.nextInt(max - min + min) + min
    }


}