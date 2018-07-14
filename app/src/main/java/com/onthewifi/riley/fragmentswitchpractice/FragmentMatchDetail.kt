package com.onthewifi.riley.fragmentswitchpractice

import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class FragmentMatchDetail: Fragment() {
    private var fragmentTag = "match_detail"
    private lateinit var parent: MainActivity
    private lateinit var baseView: ConstraintLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        baseView = inflater.inflate(R.layout.fragment_match_detail,container,false) as ConstraintLayout
        parent = activity as MainActivity
        return baseView
    }

}