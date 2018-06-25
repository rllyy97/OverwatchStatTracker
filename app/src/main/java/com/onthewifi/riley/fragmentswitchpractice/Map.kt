package com.onthewifi.riley.fragmentswitchpractice

enum class Map(private var friendlyName: String, private var type: String) {
    // Assault
    Hanamura("Hanamura", "assault"),
    HorizonLunarColony("Horizon Lunar Colony", "assault"),
    TempleOfAnubis("Temple of Anubis", "assault"),
    VolskayaIndustries("Volskaya Industries", "assault"),
    // Escort
    Dorado("Dorado", "escort"),
    Junkertown("Junkertown", "escort"),
    Rialto("Rialto", "escort"),
    Route66("Route66","escort"),
    WatchpointGibraltar("Watchpoint: Gibraltar", "escort"),
    // Combination
    BlizzardWorld("Blizzard World", "combination"),
    Eichenwalde("Eichenwalde", "combination"),
    Hollywood("Hollywood", "combination"),
    KingsRow("King's Row", "combination"),
    Numbani("Numbani", "combination"),
    // Control
    Ilios("Ilios", "control"),
    LijiangTower("Lijiang Tower", "control"),
    Nepal("Nepal", "control"),
    Oasis("Oasis", "control");

    override fun toString(): String {
        return this.friendlyName
    }

    fun getType(): String {
        return this.type
    }




}