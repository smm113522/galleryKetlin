package com.kotlin.demo.activity

import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.widget.AbsListView
import com.bumptech.glide.Glide
import com.kotlin.demo.IOnItemClick
import com.kotlin.demo.R
import com.kotlin.demo.adapter.SingleAlbumAdapter
import com.kotlin.demo.base.BaseActivity

/**
 * Created by Administrator on 2017/12/31 0031.
 */
class AlbumsActivity : BaseActivity(), IOnItemClick {

    var my_album_toolbar: Toolbar? = null
    var rvAlbumSelected: RecyclerView? = null

    override fun getLayoutId(): Int {
        return R.layout.activity_album
    }

    override fun init() {
        my_album_toolbar = findViewById(R.id.my_album_toolbar) as Toolbar?
        rvAlbumSelected = findViewById(R.id.rvAlbumSelected) as RecyclerView?

        setSupportActionBar(my_album_toolbar)

        // Enable the Up button
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val folder_name = intent.getStringExtra("folder_name")
        supportActionBar!!.setTitle("" + folder_name)
        val isVideo = intent.getBooleanExtra("isVideo", false)
        init_ui_views(folder_name, isVideo)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // TODO Auto-generated method stub
        if (item.getItemId() === android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onItemClick(position: String, isVideo: Boolean) {
        val intent = Intent(this, PhotoActivity::class.java)
        intent.putExtra("folder_name", position)
        startActivity(intent)
    }

    var adapter: SingleAlbumAdapter? = null

    private fun init_ui_views(folderName: String?, isVideo: Boolean?) {

//        val options = RequestOptions()
//                .diskCacheStrategy(DiskCacheStrategy.RESOURCE).override(160, 160).skipMemoryCache(true).error(R.drawable.ic_image_unavailable)
        val glide = Glide.with(this)
//        val builder = glide.asBitmap()

        rvAlbumSelected!!.layoutManager = GridLayoutManager(this, 2)
        rvAlbumSelected?.setHasFixedSize(true)
        adapter = SingleAlbumAdapter(getAllShownImagesPath(this, folderName, isVideo), this, glide, this)
        rvAlbumSelected?.adapter = adapter

        rvAlbumSelected?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                when (newState) {
                    RecyclerView.SCROLL_STATE_IDLE -> glide.resumeRequests()
                    AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL, AbsListView.OnScrollListener.SCROLL_STATE_FLING -> glide.pauseRequests()
                }
            }
        }
        )
    }

// Read all images path from specified directory.

    private fun getAllShownImagesPath(activity: Activity, folderName: String?, isVideo: Boolean?): MutableList<String> {

        val uri: Uri
        val cursorBucket: Cursor
        val column_index_data: Int
        val listOfAllImages = ArrayList<String>()
        var absolutePathOfImage: String? = null

        val selectionArgs = arrayOf("%" + folderName + "%")

        uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val selection = MediaStore.Images.Media.DATA + " like ? "

        val projectionOnlyBucket = arrayOf(MediaStore.MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME)

        cursorBucket = activity.contentResolver.query(uri, projectionOnlyBucket, selection, selectionArgs, null)

        column_index_data = cursorBucket.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)

        while (cursorBucket.moveToNext()) {
            absolutePathOfImage = cursorBucket.getString(column_index_data)
            if (absolutePathOfImage != "" && absolutePathOfImage != null)
                listOfAllImages.add(absolutePathOfImage)
        }
        return listOfAllImages.asReversed()
    }

}