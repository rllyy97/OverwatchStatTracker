package com.onthewifi.riley.fragmentswitchpractice

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent

class LockableViewPager : ViewPager {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    private var locked = true

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return if(locked) false
        else super.onInterceptTouchEvent(ev)
    }
    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        return if(locked) false
        else performClick()
    }

    override fun performClick(): Boolean {
        return if(locked) false
        else super.performClick()
    }
}