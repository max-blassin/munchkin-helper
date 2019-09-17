package com.blassin.max.munchkinhelper.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

const val DATABASE_VERSION = 1

@Database(
    entities = [
        Player::class
    ],
    version = DATABASE_VERSION
)
abstract class Database: RoomDatabase() {
    abstract fun playerDao(): PlayerDAO
}