package com.onthewifi.riley.fragmentswitchpractice

enum class Map(private var friendlyName: String, private var type: String) {

    BlizzardWorld("Blizzard World", "combination"),
    Dorado("Dorado", "escort"),
    Eichenwalde("Eichenwalde", "combination"),
    Hanamura("Hanamura", "assault"),
    Hollywood("Hollywood", "combination"),
    HorizonLunarColony("Horizon Lunar Colony", "assault"),
    Ilios("Ilios", "control"),
    Junkertown("Junkertown", "escort"),
    KingsRow("King's Row", "combination"),
    LijiangTower("Lijiang Tower", "control"),
    Nepal("Nepal", "control"),
    Numbani("Numbani", "combination"),
    Oasis("Oasis", "control"),
    Rialto("Rialto", "escort"),
    Route66("Route 66","escort"),
    TempleOfAnubis("Temple of Anubis", "assault"),
    VolskayaIndustries("Volskaya Industries", "assault"),
    WatchpointGibraltar("Watchpoint: Gibraltar", "escort");

    override fun toString(): String {
        return this.friendlyName
    }

    fun getType(): String {
        return this.type
    }




}