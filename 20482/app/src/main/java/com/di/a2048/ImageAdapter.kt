package com.di.a2048

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView

// references to our images
private val mThumbIds = arrayOf<Int>(
        R.drawable.tile_background, R.drawable.tile_background,
        R.drawable.tile_background, R.drawable.tile_background,
        R.drawable.tile_background, R.drawable.tile_background,
        R.drawable.tile_background, R.drawable.tile_background,
        R.drawable.tile_background, R.drawable.tile_background,
        R.drawable.tile_background, R.drawable.tile_background,
        R.drawable.tile_background, R.drawable.tile_background,
        R.drawable.tile_background, R.drawable.tile_background)

private val mTileUnit = R.layout.custom_grid_tiles

class ImageAdapter(private val mContext: Context) : BaseAdapter() {

    override fun getCount(): Int = mThumbIds.size

    override fun getItem(position: Int): Any? = null

    override fun getItemId(position: Int): Long = 0L

    // create a new ImageView for each item referenced by the Adapter
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val imageView: ImageView
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = ImageView(mContext)
            imageView.layoutParams = ViewGroup.LayoutParams(235, 264)
            imageView.scaleType = ImageView.ScaleType.CENTER_CROP
            imageView.setPadding(16, 16, 16, 16)
        } else {
            imageView = convertView as ImageView
        }

        imageView.setImageResource(mThumbIds[position])
        return imageView
    }
}