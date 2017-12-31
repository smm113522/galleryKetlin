package com.kotlin.demo.base

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import butterknife.ButterKnife

/**
 * Created by Administrator on 2017/12/6 0006.
 */
abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        ButterKnife.bind(this);
        init()
    }

   protected abstract fun getLayoutId(): Int
   protected abstract fun init()

}