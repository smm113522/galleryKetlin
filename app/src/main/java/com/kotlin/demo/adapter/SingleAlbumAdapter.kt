package com.kotlin.demo.adapter

import android.content.Context
import android.graphics.Bitmap
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.RequestManager
import com.kotlin.demo.IOnItemClick
import com.kotlin.demo.R

/**
 * Created by Administrator on 2017/12/31 0031.
 */
class SingleAlbumAdapter(val albumList: MutableList<String>, val context: Context,
                         val glideMain: RequestManager,
                         val inOnItemClick: IOnItemClick) : RecyclerView.Adapter<SingleAlbumAdapter.ViewHolder>() {


    override fun onViewRecycled(holder: ViewHolder?) {
        if (holder != null) {
        }
        super.onViewRecycled(holder)
    }

    override fun onViewDetachedFromWindow(holder: ViewHolder) {
        if (holder != null) {

        }
        super.onViewDetachedFromWindow(holder)
    }

    override fun getItemCount(): Int {
        return albumList.size
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.bindItems(albumList.get(position), glideMain, inOnItemClick)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_single_album_layout, parent, false)
        return ViewHolder(v)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var thumbnail: ImageView = itemView.findViewById(R.id.thumbnail);

        fun bindItems(albumList: String, glide: RequestManager, inOnItemClick: IOnItemClick) {

            glide.load(albumList).thumbnail(0.4f)
                    .into(thumbnail)

            itemView.setOnClickListener(object : View.OnClickListener {
                override fun onClick(p0: View?) {
                    inOnItemClick.onItemClick(albumList, false)
                }
            })
        }
    }
}