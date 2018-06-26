package com.onthewifi.riley.fragmentswitchpractice

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView

class FragmentNewMatch: Fragment(), CharacterSelectorDialog.OnInputListener{
    override fun sendInput(input: String) {
        heroStringArray[heroCounter] = input
        heroIconArray[heroCounter].setOnClickListener(deleteCharacter)
        heroIconArray[heroCounter].setImageResource(Hero.from(input)!!.getDrawable())

        heroCounter++
        if (heroCounter < 5) {
            heroIconArray[heroCounter].isClickable = true
            heroIconArray[heroCounter].setImageResource(R.drawable.ic_add_black_24dp)
        }

    }

    // Deletes character, cascades others down
    private var deleteCharacter = View.OnClickListener {
        var marker = it.tag as Int
        while (marker+1 < heroCounter) {
            heroStringArray[marker] = heroStringArray[marker+1]
            heroIconArray[marker].setImageResource(Hero.from(heroStringArray[marker]!!)!!.getDrawable())
            marker++
        }
        heroStringArray[marker] = null
        heroIconArray[marker].setOnClickListener(addCharacter)
        heroIconArray[marker].setImageResource(R.drawable.ic_add_black_24dp)
        if (heroCounter < 5) {
            marker++
            heroIconArray[marker].isClickable = false
            heroIconArray[marker].setImageResource(R.color.transparent)
        }
        heroCounter--
    }

    // Adds character
    private var addCharacter = View.OnClickListener {
        val dialog = CharacterSelectorDialog() as DialogFragment
        dialog.setTargetFragment(this, 0)
        dialog.show(fragmentManager,"dialog")
    }

    private var heroCounter = 0
    private var hero1: String? = null
    private var hero2: String? = null
    private var hero3: String? = null
    private var hero4: String? = null
    private var hero5: String? = null
    private var heroStringArray = arrayOf(hero1,hero2,hero3,hero4,hero5)

    // View References
    private lateinit var title: TextView
    private lateinit var hero1icon: ImageButton
    private lateinit var hero2icon: ImageButton
    private lateinit var hero3icon: ImageButton
    private lateinit var hero4icon: ImageButton
    private lateinit var hero5icon: ImageButton
    private lateinit var heroIconArray: Array<ImageButton>
    private lateinit var mapSpinner: Spinner

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_new_match,container,false)
        title = view.findViewById(R.id.titleTextView)
        hero1icon = view.findViewById(R.id.hero1icon)
        hero1icon.tag = 0
        hero2icon = view.findViewById(R.id.hero2icon)
        hero2icon.tag = 1
        hero3icon = view.findViewById(R.id.hero3icon)
        hero3icon.tag = 2
        hero4icon = view.findViewById(R.id.hero4icon)
        hero4icon.tag = 3
        hero5icon = view.findViewById(R.id.hero5icon)
        hero5icon.tag = 4

        mapSpinner = view.findViewById(R.id.mapSpinner)
        mapSpinner.adapter = ArrayAdapter<Map>(context, android.R.layout.simple_dropdown_item_1line, Map.values())


        heroIconArray = arrayOf(hero1icon, hero2icon, hero3icon, hero4icon, hero5icon)
        for (icon in heroIconArray) {
            icon.setOnClickListener(addCharacter)
        }
        return view
    }

}