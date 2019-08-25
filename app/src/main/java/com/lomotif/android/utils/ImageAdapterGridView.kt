package com.lomotif.android.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.lomotif.android.Interface.ClickHandler
import com.lomotif.android.R
import com.lomotif.android.model.Hit

class ImageAdapterGridView() : BaseAdapter() {

    lateinit var context: Context
    private lateinit var listHits: List<Hit>
    var clickHandler: ClickHandler<Any>? = null

    constructor(
        context: Context,
        listMedia: List<Hit>,
        clickHandler: ClickHandler<Any>?
    ) : this() {
        this.clickHandler = clickHandler
        this.context = context
        this.listHits = listMedia
    }

    override fun getCount(): Int {
        return listHits.size
    }

    override fun getItem(position: Int): Any? {
        return listHits[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val inflater =
            parent.context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.list_item_image, parent,false)

        // Get the custom view widgets reference
        val imageView = view.findViewById<ImageView>(R.id.ivHits)
        Glide.with(context)
            .load(listHits[position].previewURL)
            .asBitmap()
            .into(imageView)

        // Set a click listener for card view
        imageView.setOnClickListener {
            clickHandler?.onClick(listHits[position],position)
        }

        return view
    }
}
