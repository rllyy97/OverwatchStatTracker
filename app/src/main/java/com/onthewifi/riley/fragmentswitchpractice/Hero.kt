package com.onthewifi.riley.fragmentswitchpractice

enum class Hero(private var friendlyName: String, private var drawable: Int) {

    Ana("Ana", R.drawable.ana),
    Bastion("Bastion", R.drawable.bastion),
    Brigette("Brigette", R.drawable.brigette),
    Doomfist("Doomfist", R.drawable.doomfist),
    DVa("DVa", R.drawable.dva),
    Genji("Genji", R.drawable.genji),
    Hanzo("Hanzo", R.drawable.hanzo),
    Junkrat("Junkrat", R.drawable.junkrat),
    Lucio("Lucio", R.drawable.lucio),
    Mcree("Mcree", R.drawable.mcree),
    Mei("Mei", R.drawable.mei),
    Mercy("Mercy", R.drawable.mercy),
    Moira("Moira", R.drawable.moira),
    Orisa("Orisa", R.drawable.orisa),
    Pharah("Pharah", R.drawable.pharah),
    Reaper("Reaper", R.drawable.reaper),
    Reinhardt("Reinhardt", R.drawable.reinhardt),
    Roadhog("Roadhog", R.drawable.roadhog),
    Soldier76("Soldier76", R.drawable.soldier76),
    Sombra("Sombra", R.drawable.sombra),
    Symmetra("Symmetra", R.drawable.symmetra),
    Torbjorn("Torbjorn", R.drawable.torbjorn),
    Tracer("Tracer", R.drawable.tracer),
    Widowmaker("Widowmaker", R.drawable.widowmaker),
    Winston("Winston", R.drawable.winston),
    WreckingBall("WreckingBall", R.drawable.wreckingball),
    Zarya("Zarya", R.drawable.zarya),
    Zenyatta("Zenyatta", R.drawable.zenyatta);

    override fun toString(): String {
        return this.friendlyName
    }

    fun getDrawable(): Int {
        return this.drawable
    }

    companion object {
        fun from(name: String): Hero? = values().find { it.friendlyName == name }
    }
}