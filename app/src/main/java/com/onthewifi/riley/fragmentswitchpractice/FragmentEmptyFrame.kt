package com.onthewifi.riley.fragmentswitchpractice

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout

class FragmentEmptyFrame: Fragment()  {
    val TAG = "empty_frame"
    private lateinit var mainActivity: MainActivity
    lateinit var baseView: FrameLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        baseView = inflater.inflate(R.layout.fragment_empty_frame,container,false) as FrameLayout
        mainActivity = activity as MainActivity
        fragmentManager!!.beginTransaction().replace(baseView.id, FragmentTracker(), "tracker").commit()
        return baseView
    }

}