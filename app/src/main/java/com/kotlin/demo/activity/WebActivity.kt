package com.kotlin.demo.activity

import android.support.v7.widget.Toolbar
import android.view.KeyEvent
import android.view.KeyEvent.KEYCODE_BACK
import android.view.MenuItem
import android.webkit.WebSettings.LOAD_DEFAULT
import android.webkit.WebView
import android.webkit.WebViewClient
import com.kotlin.demo.R
import com.kotlin.demo.base.BaseActivity

/**
 * Created by Administrator on 2017/12/31 0031.
 */
class WebActivity : BaseActivity() {

    var toolbar: Toolbar? = null
    var webview: WebView? = null

    override fun getLayoutId(): Int {
        return R.layout.activity_web
    }

    override fun init() {
        toolbar = findViewById(R.id.mytoolbar) as Toolbar?
        webview = findViewById(R.id.webview) as WebView?
        setSupportActionBar(toolbar)
        // Enable the Up button
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(true)

        supportActionBar!!.setTitle("关于我")

        var websetting = webview!!.settings;
        websetting.javaScriptCanOpenWindowsAutomatically = true
        websetting.javaScriptEnabled = true
        websetting.allowContentAccess = true
        websetting.allowFileAccess = true
        websetting.domStorageEnabled = true
        websetting.setAppCacheEnabled(true)
        websetting.cacheMode = LOAD_DEFAULT

        webview!!.loadUrl("http://shimengmeng.win")
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.getItemId() === android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

/*
    override fun onBackPressed() {
        super.onBackPressed()
        if (webview!!.canGoBack()){
            webview!!.goBack()
        }
    }
*/

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if ((keyCode == KEYCODE_BACK) && webview!!.canGoBack()) {
            webview!!.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event)

    }

}

