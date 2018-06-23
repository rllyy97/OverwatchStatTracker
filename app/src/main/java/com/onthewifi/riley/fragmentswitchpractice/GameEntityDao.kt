package com.onthewifi.riley.fragmentswitchpractice

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.arch.persistence.room.Query

@Dao
interface GameEntityDao {

    @Query("SELECT * from gameData")
    fun getAll(): List<GameData>

    @Insert(onConflict = REPLACE)
    fun insert(gameData: GameData)

    @Query("DELETE from gameData")
    fun deleteAll()
}