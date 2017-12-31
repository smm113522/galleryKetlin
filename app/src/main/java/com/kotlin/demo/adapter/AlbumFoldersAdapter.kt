package com.kotlin.demo.adapter

import android.content.Context
import android.graphics.Bitmap
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.kotlin.demo.IOnItemClick
import com.kotlin.demo.R
import com.kotlin.demo.bean.Albums

/**
 * Created by Administrator on 2017/12/31 0031.
 */


class AlbumFoldersAdapter(val albumList: ArrayList<Albums>, val context: Context,
                          val glideMain: RequestManager, val inOnItemClick: IOnItemClick) : RecyclerView.Adapter<AlbumFoldersAdapter.ViewHolder>() {

    override fun onViewRecycled(holder: ViewHolder?) {
        if (holder != null) {
            //glideMain.clear(holder.itemView.thumbnail)
            // glide.clear(holder.itemView.thumbnail)
            //Glide.get(context).clearMemory()
            // holder?.itemView?.thumbnail?.setImageBitmap(null)
        }// Glide.clear(holder?.itemView?.thumbnail)
        super.onViewRecycled(holder)

    }

    override fun onViewDetachedFromWindow(holder: ViewHolder) {
        if (holder != null) {
            // glideMain.clear(holder.itemView.thumbnail)
            //Glide.get(context).clearMemory()
            // holder?.itemView?.thumbnail?.setImageBitmap(null)

        }

        super.onViewDetachedFromWindow(holder)
    }

    override fun getItemCount(): Int {
        return albumList.size
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.bindItems(albumList.get(position), glideMain, inOnItemClick, albumList.get(position).isVideo)

        holder?.title?.setText(albumList.get(position).folderNames)
        if (albumList.get(position).isVideo)
            holder?.photoCount?.setText("" + albumList.get(position).imgCount + " videos")
        else
            holder?.photoCount?.setText("" + albumList.get(position).imgCount + " photos")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        return ViewHolder(v)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView = itemView.findViewById(R.id.title);
        var photoCount: TextView = itemView.findViewById(R.id.photoCount);
        var thumbnail: ImageView = itemView.findViewById(R.id.thumbnail);
        fun bindItems(albumList: Albums, glide: RequestManager, inOnItemClick: IOnItemClick, isVideo: Boolean) {
            glide.load(albumList.imagePath).thumbnail(0.4f)
                    .into(thumbnail)

            itemView.setOnClickListener(object : View.OnClickListener {
                override fun onClick(p0: View?) {
                    inOnItemClick.onItemClick(albumList.folderNames, isVideo)
                }
            })
        }
    }
}

