package com.example.recorderapp.start

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import com.example.recorderapp.constants.Constants
import com.example.recorderapp.service.FloatingViewService

class StartActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onClick()

        if (!Settings.canDrawOverlays(this)) {
            askPermission()
        }
    }

    private fun askPermission() {
        val intent = Intent(
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            Uri.parse("package:$packageName")
        )
        startActivityForResult(intent, Constants.SYSTEM_ALERT_WINDOW_PERMISSION)
    }

    private fun onClick() {
        if (Settings.canDrawOverlays(this)) {
            startService(Intent(this@StartActivity, FloatingViewService::class.java))
            finish()
        } else {
            askPermission()
            Toast.makeText(
                this,
                "You need System Alert Window Permission to do this",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}