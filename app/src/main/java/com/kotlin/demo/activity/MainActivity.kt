package com.kotlin.demo.activity

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.NavigationView
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.AbsListView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.kotlin.demo.IOnItemClick
import com.kotlin.demo.R
import com.kotlin.demo.adapter.AlbumFoldersAdapter
import com.kotlin.demo.base.BaseActivity
import com.kotlin.demo.bean.Albums


class MainActivity : BaseActivity(), IOnItemClick {

    var rvAlbums: RecyclerView? = null;
    var toolbar: Toolbar? = null
    var navigation: NavigationView? = null
    var drawer_layout: DrawerLayout? = null
    private var mDrawerToggle: ActionBarDrawerToggle? = null
    var fab_camera: FloatingActionButton? = null

    override fun init() {

        toolbar = findViewById(R.id.my_toolbar) as Toolbar?
        rvAlbums = findViewById(R.id.rvAlbums) as RecyclerView?

        navigation = findViewById(R.id.navigation) as NavigationView?
        drawer_layout = findViewById(R.id.drawer_layout) as DrawerLayout?
        fab_camera = findViewById(R.id.fab_camera) as FloatingActionButton?

        fab_camera!!.setOnClickListener {
            Toast.makeText(this, "nihao", Toast.LENGTH_SHORT).show();
        }

        setSupportActionBar(toolbar)
        getSupportActionBar()!!.setHomeButtonEnabled(true); //设置返回键可用
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        setupNavigationView()

        //创建返回键，并实现打开关/闭监听
        mDrawerToggle = object : ActionBarDrawerToggle(this, drawer_layout, toolbar, R.string.open, R.string.close) {
            override fun onDrawerOpened(drawerView: View?) {
                super.onDrawerOpened(drawerView)

            }

            override fun onDrawerClosed(drawerView: View?) {
                super.onDrawerClosed(drawerView)

            }
        }
        (mDrawerToggle as ActionBarDrawerToggle).syncState()
        drawer_layout!!.setDrawerListener(mDrawerToggle)

//        supportActionBar!!.setHomeAsUpIndicator(resources.getDrawable(R.mipmap.meun))
//        drawer_layout_listener();
//        setupNavigationView()

        var extra = intent.extras;
        if (extra != null) {
            var extraData = extra.get("image_url_data") as ArrayList<Albums>
            select_fragment(extraData)
        }
        supportActionBar!!.setTitle("Folders")
    }

    // drawer layout click listener in Kotlin source code.
    private fun drawer_layout_listener() {

        drawer_layout!!.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerStateChanged(newState: Int) {
            }

            override fun onDrawerSlide(drawerView: View?, slideOffset: Float) {
            }

            override fun onDrawerClosed(drawerView: View?) {
//                supportActionBar!!.setHomeAsUpIndicator(resources.getDrawable(R.mipmap.meun))
            }

            override fun onDrawerOpened(drawerView: View?) {
//                supportActionBar!!.setHomeAsUpIndicator(resources.getDrawable(R.mipmap.back))
            }
        }
        )
    }

    // Navigation item click listener Kotlin source code.
    private fun setupNavigationView() {

        navigation!!.setNavigationItemSelectedListener(object : NavigationView.OnNavigationItemSelectedListener {
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                drawer_layout!!.closeDrawer(Gravity.START)
                when (item.itemId) {
                    R.id.about -> {
                        startActivity(Intent(this@MainActivity, WebActivity::class.java))
                    }
                }
                return false
            }
        })
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_main;
    }

    override fun onItemClick(position: String, isVideo: Boolean) {
        var bundle = Bundle()
        bundle.putString("folder_name", position)
        var intent = Intent(this, AlbumsActivity::class.java)
        intent.putExtra("folder_name", position)
        startActivity(intent)
    }

    private var folder_name: String = ""

    fun select_fragment(imagesList: ArrayList<Albums>) {

//        val options = RequestOptions()
//                .diskCacheStrategy(DiskCacheStrategy.RESOURCE).override(160, 160).skipMemoryCache(true).error(R.mipmap.ic_launcher)
        val glide = Glide.with(this)

        rvAlbums?.layoutManager = GridLayoutManager(this, 2)

        rvAlbums?.setHasFixedSize(true)

        // AlbumFoldersAdapter.kt is RecyclerView Adapter class. we will implement shortly.
        rvAlbums?.adapter = AlbumFoldersAdapter(imagesList, this, glide, this)


        rvAlbums?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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

}
