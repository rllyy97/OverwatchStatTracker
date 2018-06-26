package com.onthewifi.riley.fragmentswitchpractice

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class FragmentTracker: Fragment() {
//    val TAG = "Tracker"

    private lateinit var title: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_tracker,container,false)
        title = view.findViewById(R.id.titleTextView)

        return view
    }

}