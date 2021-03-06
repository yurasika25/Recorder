package com.example.recorderapp.screenshot

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.text.format.DateFormat
import android.view.View
import androidx.core.app.ActivityCompat
import com.example.recorderapp.R
import com.example.recorderapp.constants.Constants
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

class Screen : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_screen)
        verifyStoragePermission(this)
    }

    override fun onResume() {
        super.onResume()
        Handler(Looper.getMainLooper()).postDelayed({
            takeScreenShot(window.decorView.rootView, "result")
            finish()
        }, 1_000)
    }

    private fun takeScreenShot(view: View, fileName: String) {
        val date = Date()
        val format = DateFormat.format("yyyy-MM-dd hh:mm:ss", date)
        try {
            val dirPath =
                Environment.getExternalStorageDirectory().toString() + "/ScreenShotRecorder"
            val fileDir = File(dirPath)
            if (!fileDir.exists()) {
                val mkdir = fileDir.mkdir()
            }
            val path = "$dirPath/$fileName-$format.jpeg"
            view.isDrawingCacheEnabled = true
            val bitmap = Bitmap.createBitmap(view.drawingCache)
            view.isDrawingCacheEnabled = false
            val imageFile = File(path)
            val fileOutputStream = FileOutputStream(imageFile)
            val quality = 100
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, fileOutputStream)
            fileOutputStream.flush()
            fileOutputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun verifyStoragePermission(activity: Activity?) {
        val permission = ActivityCompat.checkSelfPermission(
            activity!!,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                activity,
                Constants.PERMISSION_STORAGE,
                Constants.REQUEST_EXTERNAL_STORAGE
            )
        }
    }
}
