package com.onthewifi.riley.fragmentswitchpractice

enum class Hero(private var friendlyName: String, private var id: Int) {

    Ana("Ana",0),
    Bastion("Bastion",1),
    Brigette("Brigette",2),
    Doomfist("Doomfist",3),
    DVa("DVa",4),
    Genji("Genji",5),
    Hanzo("Hanzo",6),
    Junkrat("Junkrat",7),
    Lucio("Lucio",8),
    Mcree("Mcree",9),
    Mei("Mei",10),
    Mercy("Mercy",11),
    Moira("Moira",12),
    Orisa("Orisa",13),
    Pharah("Pharah",14),
    Reaper("Reaper",15),
    Reinhardt("Reinhardt",16),
    Roadhog("Roadhog",17),
    Soldier76("Soldier76",18),
    Sombra("Sombra",19),
    Symmetra("Symmetra",20),
    Torbjorn("Torbjorn",21),
    Tracer("Tracer",22),
    Widowmaker("Widowmaker",23),
    Winston("Winston",24),
    Zarya("Zarya",25),
    Zenyatta("Zenyatta",26);

    override fun toString(): String {
        return this.friendlyName
    }

    fun getID(): Int {
        return this.id
    }
}