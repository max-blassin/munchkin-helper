package com.blassin.max.munchkinhelper.ui.character

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.blassin.max.munchkinhelper.R
import com.blassin.max.munchkinhelper.databinding.FragmentCharacterBinding

class CharacterFragment : Fragment() {
    lateinit var binding: FragmentCharacterBinding
    lateinit var viewModel: CharacterViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel = activity?.run {
             ViewModelProviders.of(this).get(CharacterViewModel::class.java)
        }!!

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_character, container, false)
        binding.lifecycleOwner = activity
        binding.viewModel = viewModel

        return binding.root
    }
}