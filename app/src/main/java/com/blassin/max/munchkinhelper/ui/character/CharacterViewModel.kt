package com.blassin.max.munchkinhelper.ui.character

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.blassin.max.munchkinhelper.data.Player
import io.reactivex.subjects.PublishSubject

class CharacterViewModel: ViewModel() {
    var player = MutableLiveData<Player>()
    val snackbarTextSubject = PublishSubject.create<String>()
    val characterKilledSubject = PublishSubject.create<Unit>()
    val killCharacterClickSubject = PublishSubject.create<Unit>()
    val arrowClickSubject = PublishSubject.create<ArrowDirection>()

    fun changeStat(statName: String, increment: Int) {
        val newPlayer = player.value!!
        when(statName) {
            "level" -> { newPlayer.level += increment }
            "stuff" -> { newPlayer.stuff += increment }
            "bonus" -> { newPlayer.bonus += increment }
        }
        if ( newPlayer.level < 1 ) { newPlayer.level = 1 }
        if ( newPlayer.stuff < 0 ) { newPlayer.stuff = 0 }

        player.postValue(newPlayer)
    }

    fun onKillCharacter() {
        val newPlayer = player.value!!
        newPlayer.stuff = 0
        newPlayer.bonus = 0

        characterKilledSubject.onNext(Unit)
        snackbarTextSubject.onNext("Character ${newPlayer.name} was killed")
        player.postValue(newPlayer)
    }

    fun onKillCharacterClick() = killCharacterClickSubject.onNext(Unit)

    fun onArrowClick(direction: ArrowDirection) = arrowClickSubject.onNext(direction)
}