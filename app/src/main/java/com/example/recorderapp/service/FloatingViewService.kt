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
import com.example.recorderapp.main.EnterActivity
import com.example.recorderapp.screenshot.Screen
import com.example.recorderapp.video.VideoRecorder

open class FloatingViewService : Service(), View.OnClickListener {

    private var mWindowManager: WindowManager? = null

    private lateinit var mFloatingView: View
    private var kapali_widget: View? = null
    private lateinit var acik_widget: View
    open lateinit var btn_card_photo: View
    private lateinit var btn_card_video: View
    private lateinit var btn_card_menu: View
    private lateinit var relativeL: View

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

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

        kapali_widget = mFloatingView.findViewById(R.id.layoutCollapsed)
        acik_widget = mFloatingView.findViewById(R.id.layoutExpanded)
        btn_card_photo = mFloatingView.findViewById(R.id.id_card_photo)
        btn_card_video = mFloatingView.findViewById(R.id.id_card_video)
        btn_card_menu = mFloatingView.findViewById(R.id.id_card_menu)
        relativeL = mFloatingView.findViewById(R.id.relativeLayoutParent)

        mFloatingView.findViewById<View>(R.id.buttonClose).setOnClickListener(this)
        acik_widget.setOnClickListener(this)

        btn_card_photo.setOnClickListener {
            val intent = Intent(this, Screen::class.java)
            startActivity(intent)
            val player = MediaPlayer.create(this, R.raw.photo_sound)
            player.start()
        }

        btn_card_menu.setOnClickListener {
            val intent = Intent(this, EnterActivity::class.java)
            startActivity(intent)
            stopSelf()
        }

        btn_card_video.setOnClickListener {
            val intent = Intent(this, VideoRecorder::class.java)
            startActivity(intent)
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
                            acik_widget.setVisibility(View.VISIBLE)
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
                acik_widget.visibility = View.GONE
            R.id.buttonClose ->
                stopSelf()
        }
    }
}