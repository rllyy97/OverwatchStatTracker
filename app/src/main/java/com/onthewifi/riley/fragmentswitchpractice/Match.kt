package com.onthewifi.riley.fragmentswitchpractice

import com.google.firebase.database.IgnoreExtraProperties
import java.util.*
import kotlin.collections.HashMap

data class Match(
        private var sr: Int,
        private var map: String,
        private var hero1: String,
        private var hero2: String,
        private var hero3: String,
        private var hero4: String,
        private var hero5: String,
        private var eliminations: Int,
        private var damage: Int,
        private var heals: Int,
        private var deaths: Int,
        private var accuracy: Int,
        private var length: Int
        ) {

    var uuid: String = ""
    var time  = Calendar.getInstance().timeInMillis
//    val hash: HashMap<String, Any> = toMap()
//
//    private fun toMap(): HashMap<String, Any> {
//        val result = HashMap<String, Any>()
//        result["sr"] = sr
//        result["map"] = map
//        result["hero1"] = hero1
//        result["hero2"] = hero2 ?: ""
//        result["hero3"] = hero3 ?: ""
//        result["hero4"] = hero4 ?: ""
//        result["hero5"] = hero5 ?: ""
//        result["eliminations"] = eliminations
//        result["damage"] = damage
//        result["heals"] = heals
//        result["deaths"] = deaths
//        result["accuracy"] = accuracy
//        result["length"] = length
//        return result
//    }
}