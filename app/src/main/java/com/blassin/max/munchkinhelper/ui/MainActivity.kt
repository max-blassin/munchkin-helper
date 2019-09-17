package com.blassin.max.munchkinhelper.ui

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.view.Menu
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.blassin.max.munchkinhelper.R
import com.blassin.max.munchkinhelper.data.Player
import com.blassin.max.munchkinhelper.ui.character.*
import com.blassin.max.munchkinhelper.ui.players.PlayersFragment
import com.blassin.max.munchkinhelper.ui.players.PlayersListViewModel
import com.google.android.material.snackbar.Snackbar
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    lateinit var binding: ViewDataBinding
    lateinit var characterViewModel: CharacterViewModel
    lateinit var playersListViewModel: PlayersListViewModel
    var optionsMenu: Menu? = null
    var characterLoaded: Boolean = false

    var characterKilledDisposable: Disposable? = null
    var killCharacterClickDisposable: Disposable? = null
    var snackbarDisplayDisposable: Disposable? = null
    var playerDeletedDisposable: Disposable? = null
    var playerCreationErrorDisposable: Disposable? = null
    var arrowClickDisposable: Disposable? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        characterViewModel = ViewModelProviders.of(this).get(CharacterViewModel::class.java)
        characterViewModel.player.observe(this, Observer { onPlayerChange(it!!) })

        playersListViewModel = ViewModelProviders.of(this).get(PlayersListViewModel::class.java)

        supportFragmentManager.beginTransaction()
            .replace(
                R.id.top_fragment,
                NoCharacterFragment()
            )
            .replace(
                R.id.bottom_fragment,
                PlayersFragment()
            )
            .commit()
    }

    override fun onStart() {
        super.onStart()

        characterKilledDisposable = characterViewModel.characterKilledSubject.subscribe{ onCharacterKilled() }
        killCharacterClickDisposable = characterViewModel.killCharacterClickSubject.subscribe { onKillCharacterClick() }
        snackbarDisplayDisposable = characterViewModel.snackbarTextSubject.subscribe { onSnackbarChange(it) }
        playerDeletedDisposable = playersListViewModel.playerDeletedSubject.subscribe { onPlayerDeleted(it) }
        playerCreationErrorDisposable = playersListViewModel.playerCreationErrorSubject.subscribe { onPlayerCreationError(it) }
        arrowClickDisposable = characterViewModel.arrowClickSubject.subscribe { onArrowClick(it) }
    }

    override fun onStop() {
        super.onStop()

        characterKilledDisposable?.dispose()
        killCharacterClickDisposable?.dispose()
        snackbarDisplayDisposable?.dispose()
        playerDeletedDisposable?.dispose()
        arrowClickDisposable?.dispose()
    }

    private fun onKillCharacterClick() {
        val player = characterViewModel.player.value!!
        val dialog = KillCharacterDialogFragment.newInstance(player.name)
        dialog.show(supportFragmentManager, "killCharacterDialogFragment")
    }

    private fun onCharacterKilled() {
        val deathSkullImageView = top_fragment.findViewById<ImageView>(R.id.death_skull)

        deathSkullImageView?.let {
            it.scaleX = 0.2f
            it.scaleY = 0.2f
            it.animate()
                .alpha(1.0f)
                .scaleX(1.0f)
                .scaleY(1.0f)
                .rotationBy(360.0f)
                .setDuration(400)
                .setListener(object: AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        it.animate()
                            .alpha(0.0f)
                            .setDuration(2000)
                            .setListener(null)
                    }
                })
        }
    }

    private fun onPlayerChange(newPlayer: Player) {
        if (!characterLoaded) {
            supportFragmentManager.beginTransaction()
                .replace(
                    R.id.top_fragment,
                    CharacterFragment()
                )
                .commit()
            characterLoaded = true
        }
        refreshTitle(newPlayer)
    }

    private fun refreshTitle(newPlayer: Player? = null) {
        if (newPlayer != null) {
            optionsMenu?.findItem(R.id.menu_main_swords)?.isVisible = true
            optionsMenu?.findItem(R.id.menu_main_power)?.title = newPlayer.power.toString()
            optionsMenu?.findItem(R.id.menu_main_power)?.isVisible = true
            title = newPlayer.name
        } else {
            optionsMenu?.findItem(R.id.menu_main_power)?.title = ""
            optionsMenu?.findItem(R.id.menu_main_power)?.isVisible = false
            optionsMenu?.findItem(R.id.menu_main_swords)?.isVisible = false
            title = getString(applicationInfo.labelRes)
        }
    }

    private fun onSnackbarChange(text: String) {
        Snackbar.make(main_activity, text, Snackbar.LENGTH_SHORT)
            .show()
    }

    private fun onPlayerDeleted(player: Player) {
        characterViewModel.player.value?.let { currentPlayer ->
            if (currentPlayer.name == player.name) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.top_fragment, NoCharacterFragment())
                    .commit()
                characterLoaded = false
                refreshTitle()
            }
        }
    }

    private fun onPlayerCreationError(title: String) {
        PlayerCreationErrorDialogFragment
            .newInstance(title)
            .show(supportFragmentManager, "")
    }

    private fun onArrowClick(direction: ArrowDirection) {
        val increment = direction.sign
        playersListViewModel.players.value?.let { players ->
            characterViewModel.player.value?.let { currentPlayer ->
                val pos = players.indexOf(currentPlayer)
                val nextPlayer = players[(pos + increment + players.size) % players.size]
                characterViewModel.player.postValue(nextPlayer)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        optionsMenu = menu
        return true
    }
}
