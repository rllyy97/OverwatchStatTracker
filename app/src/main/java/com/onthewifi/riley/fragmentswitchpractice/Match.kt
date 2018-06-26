package com.onthewifi.riley.fragmentswitchpractice

import com.google.firebase.database.IgnoreExtraProperties
import java.util.*

@IgnoreExtraProperties
class Match(private var uid: String,
            private var matchNumber: Int,
            private var sr: Int,
            private var map: String,
            private var hero1: String,
            private var hero2: String?,
            private var hero3: String?,
            private var hero4: String?,
            private var hero5: String?,
            private var eliminations: Int,
            private var damage: Int,
            private var heals: Int,
            private var deaths: Int,
            private var accuracy: Int,
            private var length: Int,
            private var blueScore: Int,
            private var redScore: Int,
            private var mate1: String?,
            private var mate2: String?,
            private var mate3: String?,
            private var mate4: String?,
            private var mate5: String?
            ) {

    private var time = Calendar.getInstance().time!!

    fun toMap(): HashMap<String, Any> {
        val result = HashMap<String, Any>()
        result["uid"] = uid
        result["time"] = time
        result["matchNumber"] = matchNumber
        result["sr"] = sr
        result["map"] = map
        result["hero1"] = hero1
        result["hero2"] = hero2 ?: ""
        result["hero3"] = hero3 ?: ""
        result["hero4"] = hero4 ?: ""
        result["hero5"] = hero5 ?: ""
        result["eliminations"] = eliminations
        result["damage"] = damage
        result["heals"] = heals
        result["deaths"] = deaths
        result["accuracy"] = accuracy
        result["length"] = length
        result["blueScore"] = blueScore
        result["redScore"] = redScore
        result["mate1"] = mate1 ?: ""
        result["mate2"] = mate2 ?: ""
        result["mate3"] = mate3 ?: ""
        result["mate4"] = mate4 ?: ""
        result["mate5"] = mate5 ?: ""
        return result
    }
}