package com.example.recorderapp.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.recorderapp.constants.Constants

class DialogVideoSize : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return activity?.let {
            ArrayList<Int>()
            val builder = AlertDialog.Builder(it)
            builder.setTitle("Horizontal Position")
                .setSingleChoiceItems(
                    Constants.items, -1
                ) { _, item ->
                    Toast.makeText(
                        activity, "Select:  ${Constants.items[item]}",
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