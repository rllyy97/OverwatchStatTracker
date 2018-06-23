package com.onthewifi.riley.fragmentswitchpractice

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.time.DayOfWeek
import java.time.Duration

@Entity(tableName = "gameData")
data class GameData(@PrimaryKey(autoGenerate = true) var id: Long?,
                    @ColumnInfo(name = "day") var dayOfWeek: DayOfWeek?,
                    @ColumnInfo(name = "sr") var sr: Int,
                    @ColumnInfo(name = "map") var map: String,
                    @ColumnInfo(name = "hero1") var hero1: String,
                    @ColumnInfo(name = "hero2") var hero2: String,
                    @ColumnInfo(name = "hero3") var hero3: String,
                    // End Game Screen Data
                    @ColumnInfo(name = "eliminations") var eliminations: Int,
                    @ColumnInfo(name = "damage") var damage: Long,
                    @ColumnInfo(name = "heals") var heals: Long,
                    @ColumnInfo(name = "deaths") var deaths: Int,
                    @ColumnInfo(name = "accuracy") var accuracy: Int,
                    @ColumnInfo(name = "damageBlocked") var damageBlocked: Long,
                    @ColumnInfo(name = "length") var length: Int, // in minutes
                    @ColumnInfo(name = "groupSize") var groupSize: Int
                    ){
    constructor():this(null,null,0,"","","",
            "",0,0,0,0,0,0,0,1)
}