package com.onthewifi.riley.fragmentswitchpractice

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter



class FragmentAdapter(fm: FragmentManager): FragmentStatePagerAdapter(fm) {
    private var fragmentList = ArrayList<Fragment>()

    fun addFragment(fragment:Fragment) {
        fragmentList.add(fragment)
    }

    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getCount(): Int {
        return fragmentList.size
    }

//    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
//        return false
//    }
//
//    override fun onTouchEvent(event: MotionEvent): Boolean {
//        return false
//    }



}