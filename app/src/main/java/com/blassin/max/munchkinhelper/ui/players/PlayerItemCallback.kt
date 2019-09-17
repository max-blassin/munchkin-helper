package com.blassin.max.munchkinhelper.ui.players

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.blassin.max.munchkinhelper.R
import com.blassin.max.munchkinhelper.data.Player
import com.blassin.max.munchkinhelper.ui.character.CharacterViewModel

class PlayerItemCallback(private val activity: FragmentActivity): ItemTouchHelper.SimpleCallback(
    ItemTouchHelper.UP or ItemTouchHelper.DOWN,
    ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
) {
    private var playersListViewModel: PlayersListViewModel
    private var characterViewModel: CharacterViewModel
    var orderChanged: Boolean = false
    var dragFrom = -1
    var dragTo = -1

    init {
        playersListViewModel = activity.run {
            ViewModelProviders.of(this).get(PlayersListViewModel::class.java)
        }
        characterViewModel = activity.run {
            ViewModelProviders.of(this).get(CharacterViewModel::class.java)
        }
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        val currentPos = viewHolder.adapterPosition
        if( dragFrom == -1 )
            dragFrom = currentPos
        dragTo = target.adapterPosition
        orderChanged = dragFrom != dragTo && dragFrom > -1 && dragTo > -1

        if (orderChanged) {
            playersListViewModel.onDragPlayer(currentPos, dragTo)
        }

        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val player = (viewHolder as PlayerItemAdapter.ViewHolder).cardView.tag as Player
        characterViewModel.snackbarTextSubject.onNext(activity.resources.getString(R.string.character_deleted, player.name))
        playersListViewModel.onDeletePlayer(player)
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        super.onSelectedChanged(viewHolder, actionState)

        if (actionState == ItemTouchHelper.ACTION_STATE_IDLE && orderChanged) {
            playersListViewModel.onInsertPlayer(dragFrom, dragTo)
            orderChanged = false
            dragFrom = -1
            dragTo = -1
        }
    }
}