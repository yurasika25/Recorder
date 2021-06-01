package com.example.recorderapp.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.recorderapp.R
import com.example.recorderapp.media.MediaFragment
import com.example.recorderapp.service.FloatingViewService

class EnterActivity : AppCompatActivity(), EnterView {

    private var presenter: EnterPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.class_two)
        presenter = EnterPresenter()
    }

    override fun onResume() {
        super.onResume()
        goToMainFragment()
    }

    override fun onPause() {
        super.onPause()
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
        startActivityForResult(intent, SYSTEM_ALERT_WINDOW_PERMISSION)
    }

    private fun onClick() {
        if (Settings.canDrawOverlays(this)) {
            startService(Intent(this@EnterActivity, FloatingViewService::class.java))
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

    companion object {
        private const val SYSTEM_ALERT_WINDOW_PERMISSION = 2084
    }

    override fun goToMainFragment() {
        val fragment: Fragment = MediaFragment.newInstance()
        val fm = supportFragmentManager
        val ft = fm.beginTransaction()
        ft.replace(R.id.mainContainer, fragment)
        ft.commit()
    }
}