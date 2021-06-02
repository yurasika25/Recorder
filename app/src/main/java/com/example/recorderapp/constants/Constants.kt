package com.example.recorderapp.constants

import android.Manifest
import android.util.SparseIntArray
import android.view.Surface

object Constants {
    const val SYSTEM_ALERT_WINDOW_PERMISSION = 2084

    const val REQUEST_CODE = 1000
    const val REQUEST_PERMISSION = 1000
    var DISPLAY_WIDTH = 720
    var DISPLAY_HEIGHT = 1280
    const val REQUEST_EXTERNAL_STORAGE = 1
    val ORIENTATION = SparseIntArray()


    init {
        ORIENTATION.append(Surface.ROTATION_0, 90)
        ORIENTATION.append(Surface.ROTATION_90, 0)
        ORIENTATION.append(Surface.ROTATION_180, 270)
        ORIENTATION.append(Surface.ROTATION_270, 180)
    }

    val PERMISSION_STORAGE = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )

    val items = arrayOf("1080P", "720P", "480P", "QVGA(320 x 240)", "QVGA(176 x 144)")
    val items_position = arrayOf("Left", "Center", "Right")
    val items_position_vertical = arrayOf("Top", "Center", "Bottom")

}