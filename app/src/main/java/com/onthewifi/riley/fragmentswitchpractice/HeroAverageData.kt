package com.onthewifi.riley.fragmentswitchpractice

import android.util.Log
import com.google.firebase.database.DataSnapshot

class HeroAverageData(hero: String?, allGameArray: ArrayList<DataSnapshot>) {

    var avgDamageMin = 0f
    var avgHealingMin = 0f
    var avgEliminationsMin = 0f
    var avgDeathsMin = 0f
    var avgDamageDeath = 0f
    var avgHealingDeath = 0f
    var avgEliminationsDeath = 0f
    var avgAccuracy = 0f

    init {
        var runningEliminations = 0f
        var runningDeaths = 0f
        var runningDamage = 0f
        var runningHeals = 0f
        var runningLength = 0f
        var runningAccuracy = 0f
        var matchCount = 0f


        for (game in allGameArray) {
            var check = 0
            for (child in game.child("heroes").children)
                if (child.value as String == hero)
                    check = 1

            if (hero == null || check == 1) {
                matchCount++
                runningEliminations += (game.child("eliminations").value as Long).toFloat()
                runningDeaths += (game.child("deaths").value as Long).toFloat()
                runningDamage += (game.child("damage").value as Long).toFloat()
                runningHeals += (game.child("heals").value as Long).toFloat()
                runningLength += (game.child("length").value as Long).toFloat()
                runningAccuracy += (game.child("accuracy").value as Long).toFloat()
            }
        }

        avgDamageMin = runningDamage / runningLength
        avgHealingMin = runningHeals / runningLength
        avgEliminationsMin = runningEliminations / runningLength
        avgDeathsMin = runningDeaths / runningLength
        avgDamageDeath = runningDamage / runningDeaths
        avgHealingDeath = runningHeals / runningDeaths
        avgEliminationsDeath = runningEliminations / runningDeaths
        avgAccuracy = runningAccuracy / matchCount

    }
}