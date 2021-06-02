package com.example.recorderapp.video

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.display.DisplayManager
import android.hardware.display.VirtualDisplay
import android.media.MediaRecorder
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.DisplayMetrics
import android.view.View
import android.widget.RelativeLayout
import android.widget.Toast
import android.widget.ToggleButton
import android.widget.VideoView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.recorderapp.R
import com.example.recorderapp.constants.Constants
import com.google.android.material.snackbar.Snackbar
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class Recorder : Activity() {


    private var mediaProjectionManager: MediaProjectionManager? = null
    private var mediaProjection: MediaProjection? = null
    private var virtualDisplay: VirtualDisplay? = null
    private var mediaProjectionCallBack: Recorder.MediaProjectionCallBack? = null
    private var mediaRecorder: MediaRecorder? = null
    private var mScreenDensity = 0
    private var rootLayout: RelativeLayout? = null
    private var toggleButton: ToggleButton? = null
    private var videoView: VideoView? = null
    private var videoUri = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.video_recorder)
        val metrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metrics)
        mScreenDensity = metrics.densityDpi
        Constants.DISPLAY_HEIGHT = metrics.heightPixels
        Constants.DISPLAY_WIDTH = metrics.widthPixels
        mediaRecorder = MediaRecorder()
        mediaProjectionManager =
            getSystemService(MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
        videoView = findViewById<View>(R.id.videoView) as VideoView
        toggleButton = findViewById<View>(R.id.toggleButton) as ToggleButton
        rootLayout = findViewById<View>(R.id.rootLayout) as RelativeLayout
        toggleButton!!.setOnClickListener { v: View? ->

            if (ContextCompat.checkSelfPermission(
                    this@Recorder,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                + ContextCompat.checkSelfPermission(
                    this@Recorder,
                    Manifest.permission.RECORD_AUDIO
                )
                != PackageManager.PERMISSION_GRANTED
            ) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        this@Recorder,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                    ||
                    ActivityCompat.shouldShowRequestPermissionRationale(
                        this@Recorder,
                        Manifest.permission.RECORD_AUDIO
                    )
                ) {
                    toggleButton!!.isChecked = false
                    Snackbar.make(rootLayout!!, "Permission", Snackbar.LENGTH_INDEFINITE)
                        .setAction(
                            "ENABLE"
                        ) { v1: View? ->
                            ActivityCompat.requestPermissions(
                                this@Recorder, arrayOf(
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.RECORD_AUDIO
                                ), Constants.REQUEST_PERMISSION
                            )
                        }.show()
                } else {
                    ActivityCompat.requestPermissions(
                        this@Recorder, arrayOf(
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.RECORD_AUDIO
                        ), Constants.REQUEST_PERMISSION
                    )
                }
            } else {
                toogleScreenShare(v)
            }
        }
    }

    private fun toogleScreenShare(v: View?) {
        if ((v as ToggleButton?)!!.isChecked) {
            initRecorder()
            recordScreen()
        } else {
            mediaRecorder!!.stop()
            mediaRecorder!!.reset()
            stopRecordScreen()
            videoView!!.visibility = View.VISIBLE
            videoView!!.setVideoURI(Uri.parse(videoUri))
            videoView!!.start()
        }
    }

    private fun recordScreen() {
        if (mediaProjection == null) {
            startActivityForResult(
                mediaProjectionManager!!.createScreenCaptureIntent(),
                Constants.REQUEST_CODE
            )
            return
        }
        virtualDisplay = createVirtualDisplay()
        mediaRecorder!!.start()
    }

    private fun createVirtualDisplay(): VirtualDisplay {
        return mediaProjection!!.createVirtualDisplay(
            "Recorder", Constants.DISPLAY_WIDTH, Constants.DISPLAY_HEIGHT, mScreenDensity,
            DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
            mediaRecorder!!.surface, null, null
        )
    }

    @SuppressLint("SimpleDateFormat")
    private fun initRecorder() {
        try {
            mediaRecorder!!.setAudioSource(MediaRecorder.AudioSource.MIC)
            mediaRecorder!!.setVideoSource(MediaRecorder.VideoSource.SURFACE)
            mediaRecorder!!.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            videoUri =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    .toString() + "/EDMTRecord_" + SimpleDateFormat("dd-MM-yyyy-hh_mm_ss")
                    .format(Date()) +
                        ".mp4"
            mediaRecorder!!.setOutputFile(videoUri)
            mediaRecorder!!.setVideoSize(Constants.DISPLAY_WIDTH, Constants.DISPLAY_HEIGHT)
            mediaRecorder!!.setVideoEncoder(MediaRecorder.VideoEncoder.H264)
            mediaRecorder!!.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            mediaRecorder!!.setVideoEncodingBitRate(512 * 1000)
            mediaRecorder!!.setVideoFrameRate(30)
            val rotation = windowManager.defaultDisplay.rotation
            val orientation = Constants.ORIENTATION[rotation + 90]
            mediaRecorder!!.setOrientationHint(orientation)
            mediaRecorder!!.prepare()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode != Constants.REQUEST_CODE) {
            Toast.makeText(this, "Unk error", Toast.LENGTH_SHORT).show()
            return
        }
        if (resultCode != RESULT_OK) {
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
            toggleButton!!.isChecked = false
            return
        }
        mediaProjectionCallBack = MediaProjectionCallBack()
        mediaProjection = mediaProjectionManager!!.getMediaProjection(resultCode, data!!)
        mediaProjection?.registerCallback(mediaProjectionCallBack, null)
        virtualDisplay = createVirtualDisplay()
        mediaRecorder!!.start()
    }

    inner class MediaProjectionCallBack : MediaProjection.Callback() {
        override fun onStop() {
            if (toggleButton!!.isChecked) {
                toggleButton!!.isChecked = false
                mediaRecorder!!.stop()
                mediaRecorder!!.reset()
            }
            mediaProjection = null
            stopRecordScreen()
            super.onStop()
        }
    }

    private fun stopRecordScreen() {
        if (virtualDisplay == null) return
        virtualDisplay!!.release()
        destroyMediaProjection()
    }

    private fun destroyMediaProjection() {
        if (mediaProjection != null) {
            mediaProjection!!.unregisterCallback(mediaProjectionCallBack)
            mediaProjection!!.stop()
            mediaProjection = null
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.REQUEST_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] + grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                toogleScreenShare(toggleButton)
            } else {
                toggleButton!!.isChecked = false
                Snackbar.make(rootLayout!!, "Permission", Snackbar.LENGTH_INDEFINITE)
                    .setAction(
                        "ENABLE"
                    ) {
                        ActivityCompat.requestPermissions(
                            this@Recorder, arrayOf(
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.RECORD_AUDIO
                            ), Constants.REQUEST_PERMISSION
                        )
                    }.show()
            }
        }
    }
}
