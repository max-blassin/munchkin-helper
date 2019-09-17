package com.blassin.max.munchkinhelper.ui.players

import android.app.Dialog
import androidx.lifecycle.ViewModel
import com.blassin.max.munchkinhelper.App
import com.blassin.max.munchkinhelper.R
import com.blassin.max.munchkinhelper.data.Player
import com.blassin.max.munchkinhelper.util.PlayerNameValidator
import io.reactivex.subjects.PublishSubject
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class PlayersListViewModel: ViewModel(),
    CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = CoroutineName("PlayersListViewModel")

    private val playerDao = App.database.playerDao()
    val players = playerDao.getAllPlayers()
    val addPlayerClickSubject = PublishSubject.create<Unit>()
    val playerDeletedSubject = PublishSubject.create<Player>()
    val playerCreationErrorSubject = PublishSubject.create<String>()
    val playerDraggedSubject = PublishSubject.create<Pair<Int, Int>>()
    val insertPlayerSubject = PublishSubject.create<Pair<Int, Int>>()

    var createPlayerDialog: Dialog? = null
    var createPlayerErrorDialog: Dialog? = null

    fun onAddPlayer(name: String) {
        createPlayerDialog?.dismiss()
        val nameValidator = PlayerNameValidator(name, players.value?.map { it.name } ?: listOf())
        if (!nameValidator.isValid()) {
            val message = nameValidator.getMessage()
            playerCreationErrorSubject.onNext(message)
            return
        }
        val playersList = players.value!!
        val highestOrder = playersList.map { it.order }.max() ?: -1
        val newPlayer = Player(name, order = highestOrder + 1)
        runBlocking(Dispatchers.IO) { playerDao.createPlayer(newPlayer) }
    }

    fun onAddPlayerClicked() = addPlayerClickSubject.onNext(Unit)

    fun onDeletePlayer(player: Player) {
        val newPlayers = players.value
        newPlayers?.run {
            runBlocking(Dispatchers.IO) {
                playerDao.deletePlayer(player)
            }
            playerDeletedSubject.onNext(player)
        }
    }

    fun onInsertPlayer(from: Int, to: Int) {
        players.value?.let {
            val list = mutableListOf<Player>()
            list.addAll(it)
            val fromPlayer = list[from]
            list.removeAt(from)
            list.add(to, fromPlayer)
            list.forEachIndexed { index, _ ->
                list[index].order = index
            }
            runBlocking(Dispatchers.IO) { playerDao.updatePlayers(list) }
            insertPlayerSubject.onNext(from to to)
        }
    }

    fun onPlayerChange(player: Player) {
        runBlocking(Dispatchers.IO) {
            playerDao.updatePlayer(player)
        }
    }

    fun onDragPlayer(from: Int, to: Int) {
        playerDraggedSubject.onNext(from to to)
    }
}