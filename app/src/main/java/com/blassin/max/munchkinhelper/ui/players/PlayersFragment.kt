package com.blassin.max.munchkinhelper.ui.players

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blassin.max.munchkinhelper.R
import com.blassin.max.munchkinhelper.data.Player
import com.blassin.max.munchkinhelper.databinding.FragmentPlayersBinding
import com.blassin.max.munchkinhelper.ui.character.AddCharacterDialogFragment
import com.blassin.max.munchkinhelper.ui.character.CharacterViewModel
import io.reactivex.disposables.Disposable

class PlayersFragment : Fragment(),
    PlayerItemListener {

    lateinit var playersListViewModel: PlayersListViewModel
    lateinit var characterViewModel: CharacterViewModel
    lateinit var binding: FragmentPlayersBinding

    lateinit var recyclerView: RecyclerView

    var addPlayerClickDisposable: Disposable? = null
    var playersDraggedDisposable: Disposable? = null
    var playersInsertedDisposable: Disposable? = null
    var playerDeletedDisposable: Disposable? = null

    var dbLoaded = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        playersListViewModel = activity?.run {
            ViewModelProviders.of(this).get(PlayersListViewModel::class.java)
        }!!
        characterViewModel = activity?.run {
            ViewModelProviders.of(this).get(CharacterViewModel::class.java)
        }!!

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_players, container, false)
        binding.lifecycleOwner = activity
        binding.viewModel = playersListViewModel

        val view = binding.root

        recyclerView = view.findViewById(R.id.players_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)


        val adapter = PlayerItemAdapter(mutableListOf())
        adapter.listener = this
        recyclerView.adapter = adapter
        activity?.run {
            val itemTouchHelper = ItemTouchHelper(PlayerItemCallback(this))
            itemTouchHelper.attachToRecyclerView(recyclerView)
        }

        playersListViewModel.players.observe(this, Observer { newPlayers ->
            onPlayersChange(newPlayers)
        })

        characterViewModel.player.observe(this, Observer { newPlayer ->
            onPlayerChange(newPlayer)
        })

        return view
    }

    override fun onStart() {
        super.onStart()
        addPlayerClickDisposable = playersListViewModel.addPlayerClickSubject.subscribe { onAddPlayerClick() }
        playersDraggedDisposable = playersListViewModel.playerDraggedSubject.subscribe { onPlayerDragged(it) }
        playersInsertedDisposable = playersListViewModel.insertPlayerSubject.subscribe { onPlayerInserted(it) }
        playerDeletedDisposable = playersListViewModel.playerDeletedSubject.subscribe { onPlayerDeleted(it) }
    }

    override fun onStop() {
        super.onStop()
        addPlayerClickDisposable?.dispose()
        playersDraggedDisposable?.dispose()
        playersInsertedDisposable?.dispose()
        playerDeletedDisposable?.dispose()
    }

    override fun onSelectPlayer(player: Player) {
        characterViewModel.player.postValue(player)
    }

    private fun onAddPlayerClick() {
        playersListViewModel.createPlayerDialog?.dismiss()
        val dialog = AddCharacterDialogFragment.newInstance()
        dialog.show(fragmentManager, "createCharacterClick")
    }

    private fun onPlayerChange(newPlayer: Player) {
        playersListViewModel.onPlayerChange(newPlayer)
        updateSelectedPos(newPlayer= newPlayer)
    }

    private fun onPlayersChange(newPlayers: List<Player>) {
        (recyclerView.adapter as? PlayerItemAdapter)?.run {
            updatePlayers(newPlayers)
        }
        updateSelectedPos(newPlayers= newPlayers)
    }

    private fun onPlayerDeleted(player: Player) {
        (recyclerView.adapter as? PlayerItemAdapter)?.run {
            characterViewModel.player.value?.let { currentPlayer ->
                playersListViewModel.players.value?.let { players ->
                    if (currentPlayer.name != player.name) {
                        if (players.indexOfFirst { it.name == player.name } < players.indexOfFirst { it.name == currentPlayer.name }) {
                            selectedPos = selectedPos!! - 1
                            notifyItemChanged(selectedPos!!)
                        }
                    } else {
                        selectedPos = null
                    }
                }
            }
        }
    }

    private fun updateSelectedPos(newPlayers: List<Player>? = null, newPlayer: Player? = null) {
        (recyclerView.adapter as? PlayerItemAdapter)?.run {
            (newPlayers ?: playersListViewModel.players.value)?.let { players ->
                (newPlayer ?: characterViewModel.player.value)?.let { player ->
                    val oldPos = selectedPos
                    selectedPos = players.indexOfFirst { it.name == player.name }
                    val pos = selectedPos!!
                    if (pos > -1) {
                        notifyItemChanged(pos)
                        recyclerView.smoothScrollToPosition(pos)
                    }
                    oldPos?.let { notifyItemChanged(it) }
                    return
                }
                selectedPos = null
            }
        }
    }

    private fun onPlayerDragged(pos: Pair<Int, Int>) {
        recyclerView.adapter?.run {
            notifyItemMoved(pos.first, pos.second)
        }
    }
    private fun onPlayerInserted(pos: Pair<Int, Int>) {
        recyclerView.adapter?.run {
            notifyItemMoved(pos.first, pos.second)
        }
    }

}