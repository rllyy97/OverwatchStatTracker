package com.onthewifi.riley.fragmentswitchpractice

import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import kotlinx.android.synthetic.main.activity_main.*

class CharacterSelectorDialog: DialogFragment() {
    interface OnInputListener {
        fun sendInput(input: String)
    }
    private lateinit var onInputListener: OnInputListener

    private lateinit var buttonArray: Array<ImageButton>
    // Button References
    private lateinit var dva: ImageButton
    private lateinit var orisa: ImageButton
    private lateinit var reinhardt: ImageButton
    private lateinit var roadhog: ImageButton
    private lateinit var winston: ImageButton
    private lateinit var zarya: ImageButton
    private lateinit var bastion: ImageButton
    private lateinit var doomfist: ImageButton
    private lateinit var genji: ImageButton
    private lateinit var hanzo: ImageButton
    private lateinit var junkrat: ImageButton
    private lateinit var mcree: ImageButton
    private lateinit var mei: ImageButton
    private lateinit var pharah: ImageButton
    private lateinit var reaper: ImageButton
    private lateinit var soldier76: ImageButton
    private lateinit var sombra: ImageButton
    private lateinit var symmetra: ImageButton
    private lateinit var torbjorn: ImageButton
    private lateinit var tracer: ImageButton
    private lateinit var widowmaker: ImageButton
    private lateinit var ana: ImageButton
    private lateinit var brigette: ImageButton
    private lateinit var lucio: ImageButton
    private lateinit var mercy: ImageButton
    private lateinit var moira: ImageButton
    private lateinit var zenyatta: ImageButton

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_character_picker_dialog, container, false)
        onInputListener = targetFragment as OnInputListener

        // There has to be a better way to do this
        dva = view.findViewById(R.id.DVa)
        dva.tag = "DVa"
        orisa = view.findViewById(R.id.Orisa)
        orisa.tag = "Orisa"
        reinhardt = view.findViewById(R.id.Reinhardt)
        reinhardt.tag = "Reinhardt"
        roadhog = view.findViewById(R.id.Roadhog)
        roadhog.tag = "Roadhog"
        winston = view.findViewById(R.id.Winston)
        winston.tag = "Winston"
        zarya = view.findViewById(R.id.Zarya)
        zarya.tag = "Zarya"
        bastion = view.findViewById(R.id.Bastion)
        bastion.tag = "Bastion"
        doomfist = view.findViewById(R.id.Doomfist)
        doomfist.tag = "Doomfist"
        genji = view.findViewById(R.id.Genji)
        genji.tag = "Genji"
        hanzo = view.findViewById(R.id.Hanzo)
        hanzo.tag = "Hanzo"
        junkrat = view.findViewById(R.id.Junkrat)
        junkrat.tag = "Junkrat"
        mcree = view.findViewById(R.id.Mcree)
        mcree.tag = "Mcree"
        mei = view.findViewById(R.id.Mei)
        mei.tag = "Mei"
        pharah = view.findViewById(R.id.Pharah)
        pharah.tag = "Pharah"
        reaper = view.findViewById(R.id.Reaper)
        reaper.tag = "Reaper"
        soldier76 = view.findViewById(R.id.Soldier76)
        soldier76.tag = "Soldier76"
        sombra = view.findViewById(R.id.Sombra)
        sombra.tag = "Sombra"
        symmetra = view.findViewById(R.id.Symmetra)
        symmetra.tag = "Symmetra"
        torbjorn = view.findViewById(R.id.Torbjorn)
        torbjorn.tag = "Torbjorn"
        tracer = view.findViewById(R.id.Tracer)
        tracer.tag = "Tracer"
        widowmaker = view.findViewById(R.id.Widowmaker)
        widowmaker.tag = "Widowmaker"
        ana = view.findViewById(R.id.Ana)
        ana.tag = "Ana"
        brigette = view.findViewById(R.id.Brigette)
        brigette.tag = "Brigette"
        lucio = view.findViewById(R.id.Lucio)
        lucio.tag = "Lucio"
        mercy = view.findViewById(R.id.Mercy)
        mercy.tag = "Mercy"
        moira = view.findViewById(R.id.Moira)
        moira.tag = "Moira"
        zenyatta = view.findViewById(R.id.Zenyatta)
        zenyatta.tag = "Zenyatta"

        buttonArray = arrayOf(dva, orisa, reinhardt, roadhog, winston, zarya, bastion, doomfist,
                genji, hanzo, junkrat, mcree, mei, pharah, reaper, soldier76, sombra, symmetra,
                torbjorn, tracer, widowmaker, ana, brigette, lucio, mercy, moira, zenyatta)

        for(item in buttonArray) {
            item.setOnClickListener {
                val input = it.tag as String
                onInputListener.sendInput(input)
                dialog.dismiss() }
        }
        return view
    }

//    override fun onAttach(context: Context?) {
//        super.onAttach(context)
//        onInputListener = targetFragment as OnInputListener
//    }
}