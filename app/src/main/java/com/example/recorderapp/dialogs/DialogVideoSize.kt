package com.example.recorderapp.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.DialogFragment

class DialogVideoSize : DialogFragment() {

    private val items = arrayOf("1080P", "720P", "480P", "QVGA(320 x 240)", "QVGA(176 x 144)")

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return activity?.let {
            ArrayList<Int>()
            val builder = AlertDialog.Builder(it)
            builder.setTitle("Horizontal Position")
                .setSingleChoiceItems(
                    items, -1
                ) { dialog, item ->
                    Toast.makeText(
                        activity, "Select:  ${items[item]}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                .setPositiveButton(
                    "OK"
                ) { dialog, id ->
                }

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}