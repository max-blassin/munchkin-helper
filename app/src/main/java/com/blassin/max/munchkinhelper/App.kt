package com.blassin.max.munchkinhelper

import android.app.Application
import androidx.room.Room
import com.blassin.max.munchkinhelper.data.Database

private const val databaseName = "MunchkinHelperDB"

class App: Application() {
    companion object {
        lateinit var instance: App
        lateinit var database: Database
    }

    override fun onCreate() {
        super.onCreate()

        instance = this
        database = Room.databaseBuilder(
            this,
            Database::class.java,
            databaseName
        ).build()
    }
}