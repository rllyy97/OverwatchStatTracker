package com.onthewifi.riley.fragmentswitchpractice

import com.google.firebase.database.IgnoreExtraProperties
import java.util.*
import kotlin.collections.HashMap

data class Match(
        var sr: Int,
        var map: String,
        var hero1: String,
        var hero2: String,
        var hero3: String,
        var hero4: String,
        var hero5: String,
        var eliminations: Int,
        var damage: Int,
        var heals: Int,
        var deaths: Int,
        var accuracy: Int,
        var length: Int
        ) {

    var uuid: String = ""
    var time  = Calendar.getInstance().timeInMillis


}