package com.example.recorderapp.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.recorderapp.constants.Constants

class PositionDialogFragmentVertical : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return activity?.let {
            ArrayList<Int>()
            val builder = AlertDialog.Builder(it)
            builder.setTitle("Vertical Position")
                .setSingleChoiceItems(
                    Constants.items_position_vertical, -1
                ) { _, item ->
                    Toast.makeText(
                        activity, "Select:  ${Constants.items_position_vertical[item]}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                .setPositiveButton(
                    "OK"
                ) { _, _ ->
                }

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}