package com.onthewifi.riley.fragmentswitchpractice

data class Match(
        var time: Long,
        var winRate: Float,
        var sr: Int,
        var win: Int,
        var map: String,
        var heroes: ArrayList<String>,
        var eliminations: Int,
        var damage: Int,
        var heals: Int,
        var deaths: Int,
        var accuracy: Int,
        var length: Int
        )