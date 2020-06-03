package com.learn.minesweeper.utils

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.learn.minesweeper.R

class GameOverDialog(var replayCallback: () -> Unit ): DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(it)
            builder.setMessage(R.string.game_over)
                .setPositiveButton(R.string.replay
                ) { dialog, id ->
                    replayCallback()
                }
                .setNeutralButton(R.string.ok
                ) { dialog, id ->
                    // User cancelled the dialog
                }
            // Create the AlertDialog object and return it
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}