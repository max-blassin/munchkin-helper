package com.blassin.max.munchkinhelper.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*

private const val TABLE_NAME = "Player"

private const val ORDER_COLNAME = "order"

private const val GET_ALL_QUERY = "SELECT * FROM $TABLE_NAME ORDER BY list_order"


@Dao
interface PlayerDAO {
    @Query(GET_ALL_QUERY)
    fun getAllPlayers(): LiveData<List<Player>>

    @Update
    suspend fun updatePlayer(player: Player)

    @Update
    suspend fun updatePlayers(players: List<Player>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createPlayer(player: Player): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createPlayers(players: List<Player>): List<Long>

    @Delete
    suspend fun deletePlayer(player: Player)
}