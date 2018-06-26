package com.onthewifi.riley.fragmentswitchpractice

enum class Hero(private var friendlyName: String, private var id: Int, private var drawable: Int) {

    Ana("Ana",0, R.drawable.ana),
    Bastion("Bastion",1, R.drawable.bastion),
    Brigette("Brigette",2, R.drawable.brigette),
    Doomfist("Doomfist",3, R.drawable.doomfist),
    DVa("DVa",4, R.drawable.dva),
    Genji("Genji",5, R.drawable.genji),
    Hanzo("Hanzo",6, R.drawable.hanzo),
    Junkrat("Junkrat",7, R.drawable.junkrat),
    Lucio("Lucio",8, R.drawable.lucio),
    Mcree("Mcree",9, R.drawable.mcree),
    Mei("Mei",10, R.drawable.mei),
    Mercy("Mercy",11, R.drawable.mercy),
    Moira("Moira",12, R.drawable.moira),
    Orisa("Orisa",13, R.drawable.orisa),
    Pharah("Pharah",14, R.drawable.pharah),
    Reaper("Reaper",15, R.drawable.reaper),
    Reinhardt("Reinhardt",16, R.drawable.reinhardt),
    Roadhog("Roadhog",17, R.drawable.roadhog),
    Soldier76("Soldier76",18, R.drawable.soldier76),
    Sombra("Sombra",19, R.drawable.sombra),
    Symmetra("Symmetra",20, R.drawable.symmetra),
    Torbjorn("Torbjorn",21, R.drawable.torbjorn),
    Tracer("Tracer",22, R.drawable.tracer),
    Widowmaker("Widowmaker",23, R.drawable.widowmaker),
    Winston("Winston",24, R.drawable.winston),
    Zarya("Zarya",25, R.drawable.zarya),
    Zenyatta("Zenyatta",26, R.drawable.zenyatta);

    override fun toString(): String {
        return this.friendlyName
    }

    fun getID(): Int {
        return this.id
    }

    fun getDrawable(): Int {
        return this.drawable
    }

    companion object {
        fun from(name: String): Hero? = values().find { it.friendlyName == name }
    }
}