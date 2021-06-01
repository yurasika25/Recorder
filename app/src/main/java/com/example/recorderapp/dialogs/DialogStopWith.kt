package com.example.recorderapp.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.DialogFragment

class DialogStopWith : DialogFragment() {

    private val catNames = arrayOf("Device shake")
    private val checkedItems = booleanArrayOf(false)

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle("Stop with")
                .setMultiChoiceItems(catNames, checkedItems) { dialog, which, isChecked ->
                    checkedItems[which] = isChecked
                    val name = catNames[which] // Get the clicked item
                    Toast.makeText(activity, name, Toast.LENGTH_LONG).show()
                }
                .setPositiveButton(
                    "OK"
                ) { dialog, id ->
                    // User clicked OK, so save the selectedItems results somewhere
                    for (i in catNames.indices) {
                        val checked = checkedItems[i]
                        if (checked) {
                            Log.i("Dialog", catNames[i])
                        }
                    }
                }
                .setNegativeButton("CANCEL") { dialog, _ ->
                    dialog.cancel()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}