package com.blassin.max.munchkinhelper.ui.character

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import com.blassin.max.munchkinhelper.R
import com.blassin.max.munchkinhelper.ui.players.PlayersListViewModel

class PlayerCreationErrorDialogFragment(val title: String): DialogFragment() {
    companion object {
        fun newInstance(playerName: String) : PlayerCreationErrorDialogFragment {
            return PlayerCreationErrorDialogFragment(playerName)
        }
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(context!!)
        val viewModel = activity?.run {
            ViewModelProviders.of(this).get(PlayersListViewModel::class.java)
        }!!
        val dialog = builder
            .setTitle(title)
            .setPositiveButton("OK") { _, _ ->
                AddCharacterDialogFragment.newInstance().show(fragmentManager, null)
            }
            .create()

        viewModel.createPlayerErrorDialog = dialog
        return dialog
    }
}