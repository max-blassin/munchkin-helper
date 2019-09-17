package com.blassin.max.munchkinhelper.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlin.math.max

@Entity(
    tableName = "Player"
)
data class Player(
    @PrimaryKey
    val name: String,
    val id: Long = 0,
    var stuff: Int = 0,
    var level: Int = 1,
    @ColumnInfo(name = "list_order")
    var order: Int = 0
) {
    @Ignore
    var power: Int = 0
        get() = max(stuff + level + bonus, 0)
        private set

    @Ignore
    var bonus: Int = 0
}