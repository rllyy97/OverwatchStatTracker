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
    private lateinit var heroImage : ImageView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_profile,container,false)
        title = view.findViewById(R.id.titleTextView)
        title.text = getString(R.string.profile_title).format((activity as MainActivity).name)
        sr = view.findViewById(R.id.srLarge)
        sr.text = (activity as MainActivity).sr.toString()
        heroImage = view.findViewById(R.id.heroImage)
        // Sets background image, there must be a better way
        if ((activity as MainActivity).mainHero == null) heroImage.setImageDrawable(ResourcesCompat.getDrawable(resources, Hero.from((activity as MainActivity).mainHero)!!.getDrawable(), null))
        else heroImage.setImageDrawable(ResourcesCompat.getDrawable(resources, R.color.transparent, null))
        return view
    }

}