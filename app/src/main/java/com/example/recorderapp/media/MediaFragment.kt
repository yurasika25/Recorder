package com.example.recorderapp.media

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.recorderapp.R
import com.example.recorderapp.dialogs.DialogStopWith
import com.example.recorderapp.dialogs.DialogVideoSize
import com.example.recorderapp.dialogs.PositionDialogFragmentHorizontal
import com.example.recorderapp.dialogs.PositionDialogFragmentVertical

class MediaFragment : Fragment(), MediaFragmentView {

    private var presenter: MediaFragmentPresenter? = null

    companion object {
        fun newInstance(): MediaFragment {
            val args = Bundle()
            val fragment = MediaFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onPause() {
        super.onPause()
        presenter?.exitFromView()
    }

    override fun onResume() {
        super.onResume()
        presenter?.enterWithView(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        presenter = MediaFragmentPresenter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view: View = inflater.inflate(R.layout.fragment_media, container, false)

        val horizontal = view.findViewById<View>(R.id.id_horizontal_position)
        horizontal.setOnClickListener {
            val myDialogFragment = PositionDialogFragmentHorizontal()
            val manager = fragmentManager
            if (manager != null) {
                myDialogFragment.show(manager, "MyDialog")
            }
        }
        val vertical = view.findViewById<View>(R.id.id_vertical_position)
        vertical.setOnClickListener {
            val myDialogFragment = PositionDialogFragmentVertical()
            val manager = fragmentManager
            if (manager != null) {
                myDialogFragment.show(manager, "MyDialog")
            }
        }
        val videoSize = view.findViewById<View>(R.id.id_video_size)
        videoSize.setOnClickListener {
            val myDialogFragment = DialogVideoSize()
            val manager = fragmentManager
            if (manager != null) {
                myDialogFragment.show(manager, "MyDialog")
            }
        }
        val stopWith = view.findViewById<View>(R.id.id_stop_with)
        stopWith.setOnClickListener {
            val myDialogFragment = DialogStopWith()
            val manager = fragmentManager
            if (manager != null) {
                myDialogFragment.show(manager, "MyDialog")
            }
        }

        return view
    }
}