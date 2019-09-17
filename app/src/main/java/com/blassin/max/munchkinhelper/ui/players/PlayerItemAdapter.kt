package com.blassin.max.munchkinhelper.ui.players

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.blassin.max.munchkinhelper.R
import com.blassin.max.munchkinhelper.data.Player
import com.blassin.max.munchkinhelper.util.PlayersChangedCallback

interface PlayerItemListener {
    fun onSelectPlayer(player: Player)
}

class PlayerItemAdapter(val players: MutableList<Player>):
    RecyclerView.Adapter<PlayerItemAdapter.ViewHolder>(),
    View.OnClickListener {

    var listener: PlayerItemListener? = null
    var selectedPos: Int? = null

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val levelTextView: TextView = itemView.findViewById(R.id.item_player_level)
        val powerTextView: TextView = itemView.findViewById(R.id.item_player_power)
        val stuffTextView: TextView = itemView.findViewById(R.id.item_player_stuff)
        val nameTextView: TextView = itemView.findViewById(R.id.item_player_name)
        val cardView: CardView = itemView.findViewById(R.id.item_player_cardview)
    }

    fun updatePlayers(newPlayers: List<Player>) {
        players.clear()
        players.addAll(newPlayers)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_player, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = players.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val player = players[position]
        with(holder) {
            levelTextView.text = player.level.toString()
            powerTextView.text = player.power.toString()
            stuffTextView.text = player.stuff.toString()
            nameTextView.text = player.name
            cardView.tag = player
            cardView.setOnClickListener(this@PlayerItemAdapter)
            if (position == selectedPos) {
                cardView.setBackgroundResource(R.color.colorPrimaryLight)
            } else {
                cardView.setBackgroundResource(R.color.white)
            }
        }
    }

    override fun onClick(cardView: View) {
        (cardView.tag as? Player)?.let {
            listener?.onSelectPlayer(it.copy())
        }
    }

    override fun getItemId(position: Int): Long {
        return players[position].name.hashCode().toLong()
    }
}
