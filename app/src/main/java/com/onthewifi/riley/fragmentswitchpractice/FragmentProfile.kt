package com.onthewifi.riley.fragmentswitchpractice

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.res.ResourcesCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

class FragmentProfile: Fragment() {
//    val TAG = "Home"

    private lateinit var title : TextView
    private lateinit var sr : TextView
    private lateinit var settingsButton : Button
    private lateinit var heroImage : ImageView


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_profile,container,false)
        title = view.findViewById(R.id.titleTextView)
        title.text = getString(R.string.profile_title).format((activity as MainActivity).name)
        sr = view.findViewById(R.id.srLarge)
        sr.text = (activity as MainActivity).sr.toString()
        settingsButton = view.findViewById(R.id.settingsButton)
        settingsButton.setOnClickListener(settingsClick)
        heroImage = view.findViewById(R.id.heroImage)
        // Sets background image, there must be a better way
        when ((activity as MainActivity).mainHero) {
            "Ana" -> heroImage.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ana, null))
            "Bastion" -> heroImage.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.bastion, null))
            "Brigette" -> heroImage.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.brigette, null))
            "Doomfist" ->heroImage.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.doomfist, null))
            "DVa" -> heroImage.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.dva, null))
            "Genji" -> heroImage.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.genji, null))
            "Hanzo" -> heroImage.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.hanzo, null))
            "Junkrat" -> heroImage.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.junkrat, null))
            "Lúcio" -> heroImage.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.lucio, null))
            "Mcree" -> heroImage.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.mcree, null))
            "Mei" -> heroImage.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.mei, null))
            "Mercy" -> heroImage.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.mercy, null))
            "Moira" -> heroImage.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.moira, null))
            "Orisa" -> heroImage.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.orisa, null))
            "Pharah" -> heroImage.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.pharah, null))
            "Reaper" -> heroImage.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.reaper, null))
            "Reinhardt" -> heroImage.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.reinhardt, null))
            "Roadhog" -> heroImage.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.roadhog, null))
            "Soldier76" -> heroImage.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.soldier76, null))
            "Sombra" -> heroImage.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.sombra, null))
            "Symmetra" -> heroImage.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.symmetra, null))
            "Torbjörn" -> heroImage.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.torbjorn, null))
            "Tracer" -> heroImage.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.tracer, null))
            "Widowmaker" -> heroImage.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.widowmaker, null))
            "Winston" -> heroImage.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.winston, null))
            "Zarya" -> heroImage.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.zarya, null))
            "Zenyatta" -> heroImage.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.zenyatta, null))
        }
//        Toast.makeText(context,  (activity as MainActivity).mainHero , Toast.LENGTH_SHORT).show()
        return view
    }

    private val settingsClick: View.OnClickListener = View.OnClickListener {
        startActivity(Intent(context, InitializeActivity::class.java))
        (activity as MainActivity).finish()
    }

}