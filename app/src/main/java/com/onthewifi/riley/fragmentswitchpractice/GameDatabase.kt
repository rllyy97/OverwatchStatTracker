package com.onthewifi.riley.fragmentswitchpractice

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context

@Database(entities = [(GameData::class)], version = 1)
abstract class GameDatabase : RoomDatabase() {

    abstract fun gameEntityDao(): GameEntityDao

    companion object {
        private var INSTANCE: GameDatabase? = null

        fun getInstance(context: Context): GameDatabase? {
            if (INSTANCE == null) {
                synchronized(GameDatabase::class) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                            GameDatabase::class.java, "weather.db")
                            .build()
                }
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}