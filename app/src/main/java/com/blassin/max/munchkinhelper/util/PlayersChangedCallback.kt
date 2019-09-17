package com.blassin.max.munchkinhelper.util

import androidx.recyclerview.widget.DiffUtil
import com.blassin.max.munchkinhelper.data.Player

class PlayersChangedCallback(val oldPlayers: List<Player>, val newPlayers: List<Player>): DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldPlayers[oldItemPosition].name == newPlayers[newItemPosition].name
    }

    override fun getOldListSize(): Int = oldPlayers.size

    override fun getNewListSize(): Int = newPlayers.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldPlayers[oldItemPosition]
        val newItem = newPlayers[newItemPosition]
        return oldItem.name == newItem.name
                && oldItem.stuff == newItem.stuff
                && oldItem.level == newItem.level
                && oldItem.power == newItem.power
                && oldItem.bonus == newItem.bonus
    }
}