package com.example.recorderapp.service

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import androidx.annotation.RequiresApi
import com.example.recorderapp.R
import com.example.recorderapp.video.Recorder
import com.example.recorderapp.main.EnterActivity
import com.example.recorderapp.screenshot.Screen

open class FloatingViewService : Service(), View.OnClickListener {

    private var mWindowManager: WindowManager? = null

    private lateinit var mFloatingView: View
    private var widget: View? = null
    private lateinit var layoutExpandedWidget: View
    open lateinit var btnCardPhoto: View
    private lateinit var btnCardVideo: View
    private lateinit var btnCardMenu: View
    private lateinit var relativeLayout: View

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    @SuppressLint("InflateParams")
    @RequiresApi(api = Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()
        mFloatingView = LayoutInflater.from(this).inflate(R.layout.layout_activity_widget, null)

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )

        mWindowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        mWindowManager!!.addView(mFloatingView, params)

        widget = mFloatingView.findViewById(R.id.layoutCollapsed)
        layoutExpandedWidget = mFloatingView.findViewById(R.id.layoutExpanded)
        btnCardPhoto = mFloatingView.findViewById(R.id.id_card_photo)
        btnCardVideo = mFloatingView.findViewById(R.id.id_card_video)
        btnCardMenu = mFloatingView.findViewById(R.id.id_card_menu)
        relativeLayout = mFloatingView.findViewById(R.id.relativeLayoutParent)

        mFloatingView.findViewById<View>(R.id.buttonClose).setOnClickListener(this)
        layoutExpandedWidget.setOnClickListener(this)

        btnCardPhoto.setOnClickListener {
            val intent = Intent(this, Screen::class.java)
            startActivity(intent)
            val player = MediaPlayer.create(this, R.raw.photo_sound)
            player.start()
        }

        btnCardMenu.setOnClickListener {
            val intent = Intent(this, EnterActivity::class.java)
            startActivity(intent)
            stopSelf()
        }

        btnCardVideo.setOnClickListener {
            val intent = Intent(this, Recorder::class.java)
            startActivity(intent)
            layoutExpandedWidget.visibility = View.GONE
        }

        mFloatingView.findViewById<View>(R.id.relativeLayoutParent)
            .setOnTouchListener(object : View.OnTouchListener {
                private var initialX = 0
                private var initialY = 0
                private var initialTouchX = 0f
                private var initialTouchY = 0f

                @SuppressLint("ClickableViewAccessibility")
                override fun onTouch(v: View, event: MotionEvent): Boolean {
                    when (event.action) {
                        MotionEvent.ACTION_DOWN -> {
                            initialX = params.x
                            initialY = params.y
                            initialTouchX = event.rawX
                            initialTouchY = event.rawY
                            return true
                        }
                        MotionEvent.ACTION_UP -> {
                            layoutExpandedWidget.visibility = View.VISIBLE
                            return true
                        }
                        MotionEvent.ACTION_MOVE -> {
                            params.x = initialX + (event.rawX - initialTouchX).toInt()
                            params.y = initialY + (event.rawY - initialTouchY).toInt()
                            mWindowManager!!.updateViewLayout(mFloatingView, params)
                            return true
                        }
                    }
                    return false
                }
            })
    }

    override fun onDestroy() {
        super.onDestroy()
        mWindowManager!!.removeView(mFloatingView)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.layoutExpanded ->
                layoutExpandedWidget.visibility = View.GONE
            R.id.buttonClose ->
                stopSelf()
        }
    }
}