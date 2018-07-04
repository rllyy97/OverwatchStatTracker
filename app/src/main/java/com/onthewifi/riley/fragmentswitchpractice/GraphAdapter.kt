package com.onthewifi.riley.fragmentswitchpractice

import com.robinhood.spark.SparkAdapter

class GraphAdapter : SparkAdapter() {
    private var yData: ArrayList<Float> = ArrayList()
    private var base: Float = 50F
    private var hasBase: Boolean = true

    override fun getY(index: Int): Float {
        return yData[index]
    }

    fun setY(input: ArrayList<Float>) {
        yData.clear()
        yData = input
    }

    override fun getItem(index: Int): Any {
        return yData[index] // This is irrelevant
    }

    override fun getCount(): Int {
        return yData.size
    }

    override fun hasBaseLine(): Boolean {
        return hasBase
    }
    fun setBaseLineBoolean(input: Boolean) {
        hasBase = input
    }

    override fun getBaseLine(): Float {
        return base
    }

    fun setBase(input: Float) {
        base = input
    }

//    override fun getDataBounds(): RectF {
//        val bounds: RectF = super.getDataBounds()
//        bounds.inset(bounds.width(), bounds.height()*2)
//        return bounds
//    }
}