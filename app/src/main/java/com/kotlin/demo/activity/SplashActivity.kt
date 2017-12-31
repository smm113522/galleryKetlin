package com.kotlin.demo.activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.provider.MediaStore
import android.support.annotation.RequiresApi
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import android.widget.Toast
import com.kotlin.demo.R
import com.kotlin.demo.base.BaseActivity
import com.kotlin.demo.bean.Albums

/**
 * Created by Administrator on 2017/12/6 0006.
 */
class SplashActivity : BaseActivity() {

    internal var SPLASH_TIME_OUT = 1000

    @RequiresApi(Build.VERSION_CODES.M)
    override fun init() {

        Handler().postDelayed(
                {
                    // check if user has grannted permission to access device external storage.
                    // if not ask user for access to external storage.
                    if (!checkSelfPermission()) {
                        requestPermission()
                    } else {
                        // if permission granted read images from storage.
                        //  source code for this function can be found below.
                        loadAllImages()
                    }
                }, SPLASH_TIME_OUT.toLong())

    }

    /* handler.postDelayed(runder, 1000)
     private var runder = object : Runnable {
         override fun run() {
 //            var intent = Intent();
 //            intent.setClass(applicationContext, MainActivity::class.java)
             startActivity(Intent(applicationContext, MainActivity::class.java))
             finish()
         }
     }

     private var handler = object : Handler(){};
 */
    override fun getLayoutId(): Int {
        return R.layout.activity_welcome;
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 6036)
    }


    private fun checkSelfPermission(): Boolean {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            return false
        } else
            return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            6036 -> {
                if (grantResults.size > 0) {
                    var permissionGranted = grantResults[0] == PackageManager.PERMISSION_GRANTED
                    if (permissionGranted) {

                        // Now we are ready to access device storage and read images stored on device.

                        loadAllImages()
                    } else {
                        Toast.makeText(this, "Permission Denied! Cannot load images.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    fun loadAllImages() {
        var imagesList = getAllShownImagesPath(this)
        var intent = Intent(this, MainActivity::class.java)
        intent.putParcelableArrayListExtra("image_url_data", imagesList)
        startActivity(intent)
        finish()
    }
    /* get all image or ordeo */
    public fun getAllShownImagesPath(activity: Activity): ArrayList<Albums> {

        val uri: Uri
        val cursor: Cursor
        var cursorBucket: Cursor
        val column_index_data: Int
        val column_index_folder_name: Int
        val listOfAllImages = ArrayList<String>()
        var absolutePathOfImage: String? = null
        var albumsList = ArrayList<Albums>()
        var album: Albums? = null


        val BUCKET_GROUP_BY = "1) GROUP BY 1,(2"
        val BUCKET_ORDER_BY = "MAX(datetaken) DESC"

        uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI

        val projection = arrayOf(MediaStore.Images.ImageColumns.BUCKET_ID,
                MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
                MediaStore.Images.ImageColumns.DATE_TAKEN,
                MediaStore.Images.ImageColumns.DATA)

        cursor = activity.contentResolver.query(uri, projection, BUCKET_GROUP_BY, null, BUCKET_ORDER_BY)

        if (cursor != null) {
            column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
            column_index_folder_name = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
            while (cursor.moveToNext()) {
                absolutePathOfImage = cursor.getString(column_index_data)
                Log.d("title_apps", "bucket name:" + cursor.getString(column_index_data))

                val selectionArgs = arrayOf("%" + cursor.getString(column_index_folder_name) + "%")
                val selection = MediaStore.Images.Media.DATA + " like ? "
                val projectionOnlyBucket = arrayOf(MediaStore.MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME)

                cursorBucket = activity.contentResolver.query(uri, projectionOnlyBucket, selection, selectionArgs, null)
                Log.d("title_apps", "bucket size:" + cursorBucket.count)

                if (absolutePathOfImage != "" && absolutePathOfImage != null) {
                    listOfAllImages.add(absolutePathOfImage)
                    albumsList.add(Albums(cursor.getString(column_index_folder_name), absolutePathOfImage, cursorBucket.count, false))
                }
            }
        }
        return getListOfVideoFolders(activity, albumsList)
    }

    // This function is resposible to read all videos from all folders.
    public fun getListOfVideoFolders(activity: Activity, albumsList: ArrayList<Albums>): ArrayList<Albums> {

        var cursor: Cursor
        var cursorBucket: Cursor
        var uri: Uri
        val BUCKET_GROUP_BY = "1) GROUP BY 1,(2"
        val BUCKET_ORDER_BY = "MAX(datetaken) DESC"
        val column_index_album_name: Int
        val column_index_album_video: Int

        uri = android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI

        val projection1 = arrayOf(MediaStore.Video.VideoColumns.BUCKET_ID,
                MediaStore.Video.VideoColumns.BUCKET_DISPLAY_NAME,
                MediaStore.Video.VideoColumns.DATE_TAKEN,
                MediaStore.Video.VideoColumns.DATA)

        cursor = activity.contentResolver.query(uri, projection1, BUCKET_GROUP_BY, null, BUCKET_ORDER_BY)

        if (cursor != null) {
            column_index_album_name = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_DISPLAY_NAME)
            column_index_album_video = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
            while (cursor.moveToNext()) {
                Log.d("title_apps", "bucket video:" + cursor.getString(column_index_album_name))
                Log.d("title_apps", "bucket video:" + cursor.getString(column_index_album_video))
                val selectionArgs = arrayOf("%" + cursor.getString(column_index_album_name) + "%")

                val selection = MediaStore.Video.Media.DATA + " like ? "
                val projectionOnlyBucket = arrayOf(MediaStore.MediaColumns.DATA, MediaStore.Video.Media.BUCKET_DISPLAY_NAME)

                cursorBucket = activity.contentResolver.query(uri, projectionOnlyBucket, selection, selectionArgs, null)
                Log.d("title_apps", "bucket size:" + cursorBucket.count)

                albumsList.add(Albums(cursor.getString(column_index_album_name), cursor.getString(column_index_album_video), cursorBucket.count, true))
            }
        }
        return albumsList
    }

}