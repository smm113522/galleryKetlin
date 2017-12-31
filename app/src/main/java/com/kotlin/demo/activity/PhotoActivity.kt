package com.kotlin.demo.activity

import android.os.Handler
import android.support.design.widget.AppBarLayout
import android.support.v7.widget.Toolbar
import android.view.animation.AccelerateInterpolator
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.kotlin.demo.R
import com.kotlin.demo.base.BaseActivity

/**
 * Created by Administrator on 2017/12/31 0031.
 */
class PhotoActivity : BaseActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_photo
    }

    var toolbar: Toolbar? = null;
    var imageFullScreenView: ImageView? = null;
    var appbar: AppBarLayout? = null;
    override fun init() {

        toolbar = findViewById(R.id.toolbar) as Toolbar?
        imageFullScreenView = findViewById(R.id.imageFullScreenView) as ImageView?
        appbar = findViewById(R.id.appbar) as AppBarLayout?

        setSupportActionBar(toolbar)
        // Enable the Up button
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        val folder_name = intent.getStringExtra("folder_name")
        Glide.with(this).load(folder_name).into(imageFullScreenView)

        Handler().postDelayed(Runnable
        {
            if (supportActionBar != null)
                appbar!!.animate().translationY(-appbar!!.bottom.toFloat()).setInterpolator(AccelerateInterpolator()).start()
//            isAppBarShown = false
        }, 1500)
    }

}