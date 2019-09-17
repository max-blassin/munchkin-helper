package com.blassin.max.munchkinhelper.ui.character

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import com.blassin.max.munchkinhelper.R
import com.blassin.max.munchkinhelper.ui.players.PlayersListViewModel

class AddCharacterDialogFragment: DialogFragment() {
    companion object {
        fun newInstance() : AddCharacterDialogFragment {
            return AddCharacterDialogFragment()
        }
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(context!!)
        val viewModel = activity?.run {
            ViewModelProviders.of(this).get(PlayersListViewModel::class.java)
        }!!

        val input = EditText(context)
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)

        builder.setTitle(getString(R.string.createcharacter_title))
            .setPositiveButton("Create")
            { _, _ ->
                viewModel.onAddPlayer(input.text.toString())
            }
            .setNegativeButton("Cancel")
            { _, _ ->  }

        val dialog = builder.create()
        viewModel.createPlayerDialog = dialog
        return dialog
    }
}