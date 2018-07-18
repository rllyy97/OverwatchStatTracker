package com.onthewifi.riley.fragmentswitchpractice

import android.util.Log
import com.google.firebase.database.DataSnapshot

class HeroMatchData(private val gameSnap: DataSnapshot) {

    // Match Stats
    var damage = 0f
    var damageMin = 0f
    var damageDeath = 0f
    var damageMinPercent = 0f
    var damageDeathPercent = 0f
    var healing = 0f
    var healingMin = 0f
    var healingDeath = 0f
    var healingMinPercent = 0f
    var healingDeathPercent = 0f
    var elims = 0f
    var elimsMin = 0f
    var elimsDeath = 0f
    var elimsMinPercent = 0f
    var elimsDeathPercent = 0f
    var deaths = 0f
    var deathsMin = 0f
    var deathsDeaths = 0f // this is redundant but it makes me laugh so it stays
    var deathsMinPercent = 0f
    var deathsDeathsPercent = 0f // this is also redundant
    var length = 0f
    var accuracy = 0f
    var accuracyPercent = 0f

    var totalPercent = 0f

    init {
        length = getDataItem("length")
        accuracy = getDataItem("accuracy")
        damage = getDataItem("damage")
        healing = getDataItem("heals")
        elims = getDataItem("eliminations")
        deaths = getDataItem("deaths")

        damageMin = damage / length
        healingMin = healing / length
        elimsMin = elims / length
        deathsMin = deaths / length
        damageDeath = damage / deaths
        healingDeath = healing / deaths
        elimsDeath = elims / deaths

    }

    fun getPercents(averages: HeroAverageData) {
        damageMinPercent = (damageMin / averages.avgDamageMin) - 1f
        healingMinPercent = (healingMin / averages.avgHealingMin) - 1f
        elimsMinPercent = (elimsMin / averages.avgEliminationsMin) - 1f
        deathsMinPercent = (deathsMin / averages.avgDeathsMin) - 1f
        damageDeathPercent = (damageDeath / averages.avgDamageDeath) - 1f
        healingDeathPercent = (healingDeath / averages.avgHealingDeath) - 1f
        elimsDeathPercent = (elimsDeath / averages.avgEliminationsDeath) - 1f
        accuracyPercent = (accuracy / averages.avgAccuracy) - 1f
        getTotalPercent()
    }

    private fun getTotalPercent() {
        var running = 0f
        var itemCount = 8f
        running += damageMinPercent
        if (!healingMinPercent.isNaN()) running += healingMinPercent // BROKEN
        else itemCount--
        running += elimsMinPercent
        running += deathsMinPercent
        running += damageDeathPercent
        if (!healingDeathPercent.isNaN()) running += healingDeathPercent // BROKEN
        else itemCount--
        running += elimsDeathPercent
        running += accuracyPercent
        totalPercent = running / itemCount
    }

    private fun getDataItem(key: String): Float {
        return (gameSnap.child(key).value as Long).toFloat()
    }

}