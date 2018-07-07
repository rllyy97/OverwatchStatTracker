package com.onthewifi.riley.fragmentswitchpractice

data class Match(
        var time: Long,
        var winRate: Float,
        var sr: Int,
        var isWin: Boolean,
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
        )