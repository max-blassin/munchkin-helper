package com.blassin.max.munchkinhelper.ui.character

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import com.blassin.max.munchkinhelper.R

class KillCharacterDialogFragment: DialogFragment() {
    companion object {
        val EXTRA_CHARACTER_NAME = "com.blassin.max.EXTRA_CHARACTER_NAME"

        fun newInstance(characterName: String) : KillCharacterDialogFragment {
            val fragment = KillCharacterDialogFragment()
            fragment.arguments = Bundle().apply {
                putString(EXTRA_CHARACTER_NAME, characterName)
            }

            return fragment
        }
    }

    private lateinit var characterName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        characterName = arguments!!.getString(EXTRA_CHARACTER_NAME)!!
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(context!!)
        val viewModel = activity?.run {
            ViewModelProviders.of(this).get(CharacterViewModel::class.java)
        }!!

        builder.setTitle(getString(R.string.killcharacter_title, characterName))
            .setPositiveButton("Kill")
                { _, _ -> viewModel.onKillCharacter() }
            .setNegativeButton("Cancel")
                { _, _ ->  }

        return builder.create()
    }
}