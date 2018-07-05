package com.onthewifi.riley.fragmentswitchpractice

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.constraint.ConstraintSet
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.content.res.ResourcesCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.robinhood.spark.SparkView
import org.jetbrains.anko.find
import java.util.*


class FragmentProfile: Fragment(), SrInitDialog.OnInputListener {
    private var fragmentTag = "profile"
    private lateinit var parent: MainActivity

    // For sr init
    override fun sendInput(input: Int) {
        userPath.child("sr").setValue(input)
        userPath.child("name").setValue(parent.user!!.displayName)
    }

    private lateinit var userPath: DatabaseReference

    private lateinit var title : TextView
    private lateinit var graphInfo : TextView
    private lateinit var graphInfoTail : TextView
    private lateinit var heroImage : ImageView
    private lateinit var graph : SparkView
    private lateinit var lowMatchWarning : TextView
    private lateinit var fiftyPercentView : TextView

    private lateinit var srGraphButton : Button
    private lateinit var wrGraphButton : Button
    private var currentGraphTab : Int = 0

    private lateinit var allGraphButton : Button
    private lateinit var weeklyGraphButton : Button
    private lateinit var dailyGraphButton : Button
    private var currentGraphSpan : Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_profile,container,false)
        parent = activity as MainActivity
        userPath = parent.databaseRef.child("users").child(parent.user!!.uid)

        title = view.findViewById(R.id.titleTextView)
        graphInfo = view.findViewById(R.id.graphInfo)
        graphInfoTail = view.findViewById(R.id.graphInfoTail)
        heroImage = view.findViewById(R.id.heroImage)
        lowMatchWarning = view.findViewById(R.id.lowMatchWarning)
        fiftyPercentView = view.findViewById(R.id.fiftyPercent)
        graph = view.findViewById(R.id.mainGraph)
        srGraphButton = view.findViewById(R.id.srGraphButton)
        wrGraphButton = view.findViewById(R.id.wrGraphButton)
        allGraphButton = view.findViewById(R.id.allGraphButton)
        weeklyGraphButton = view.find(R.id.weeklyGraphButton)
        dailyGraphButton = view.findViewById(R.id.dailyGraphButton)

        srGraphButton.setOnClickListener { initGraph(0, currentGraphSpan) }
        wrGraphButton.setOnClickListener { initGraph(1, currentGraphSpan) }

        allGraphButton.setOnClickListener { initGraph(currentGraphTab, 0) }
        weeklyGraphButton.setOnClickListener { initGraph(currentGraphTab, 1) }
        dailyGraphButton.setOnClickListener { initGraph(currentGraphTab, 2) }

        srInitializationCheck()

        return view
    }

    private fun srInitializationCheck() {
        userPath.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snap: DataSnapshot) {
                if (snap.child("sr").value != null) loadData(snap)
                else initSr()
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "whoops", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun loadData(snap: DataSnapshot) {
        parent.latestSnap = snap
        parent.name = parent.user!!.displayName ?: "User"
        parent.sr = (snap.child("sr").value as Long).toInt()
        parent.winRate = (snap.child("winRate").value as Double).toFloat()
        parent.matchCount = (snap.child("matchCount").value as Long).toInt()
        if(parent.name.last() != 's') title.text = getString(R.string.profile_title).format(parent.name)
        else title.text = getString(R.string.profile_title_s).format(parent.name)
        graphInfo.text = parent.sr.toString()

        if (parent.mainHero == null) heroImage.setImageDrawable(ResourcesCompat.getDrawable(resources, Hero.from(parent.mainHero)!!.getDrawable(), null))
        else heroImage.setImageDrawable(ResourcesCompat.getDrawable(resources, R.color.transparent, null))

        initGraph(0, 1)
    }

    fun initSr() {
        val dialog = SrInitDialog() as DialogFragment
        dialog.setTargetFragment(this, 0)
        dialog.show(fragmentManager,"dialog")
    }

    // Graph functions
    @SuppressLint("SetTextI18n")
    private fun initGraph(tab: Int, span: Int) {
        currentGraphTab = tab
        currentGraphSpan = span
        val newAdapter = GraphAdapter()
        var color = 0
        val currentTime = Calendar.getInstance().timeInMillis

        val floatArray: ArrayList<Float> = ArrayList()
        parent.latestSnap!!.child("matches").children.forEach {
            when (currentGraphSpan) {
                0 -> { // All
                    floatArray.add(addValue(currentGraphTab, it))
                    allGraphButton.alpha = 1.0F
                    weeklyGraphButton.alpha = 0.5F
                    dailyGraphButton.alpha = 0.5F
                }
                1 -> { // Weekly
                    if(it.key!!.toLong() > (currentTime - 7 * 24 * 60 * 60 * 1000))
                        floatArray.add(addValue(currentGraphTab, it))
                    allGraphButton.alpha = 0.5F
                    weeklyGraphButton.alpha = 1.0F
                    dailyGraphButton.alpha = 0.5F
                }
                2 -> { // Daily
                    if(it.key!!.toLong() > (currentTime - 24 * 60 * 60 * 1000))
                        floatArray.add(addValue(currentGraphTab, it))
                    allGraphButton.alpha = 0.5F
                    weeklyGraphButton.alpha = 0.5F
                    dailyGraphButton.alpha = 1.0F
                }
            }
        }

        if (floatArray.size < 2) { // Not enough matches to display graph
            graph.alpha = 0.0F
            lowMatchWarning.alpha = 1.0F
            graph.isScrubEnabled = false
        } else {
            graph.alpha = 1.0F
            lowMatchWarning.alpha = 0.0F
            graph.isScrubEnabled = true
        }

        graph.scrubListener = SparkView.OnScrubListener {
            if (it != null) {
                when (currentGraphTab) {
                    0 -> graphInfo.text = it.toString()
                    1 -> graphInfo.text =  "%.2f".format(it)
                }
            } else {
                when (currentGraphTab) {
                    0 -> graphInfo.text = parent.sr.toString()
                    1 -> graphInfo.text = "%.2f".format(parent.winRate*100F)
                }
            }
        }


        when (currentGraphTab) {
            0 -> { // SR
                color = ContextCompat.getColor(parent.baseContext, R.color.colorAccent)
                wrGraphButton.alpha = 0.5F
                srGraphButton.alpha = 1F
                graphInfo.text = parent.sr.toString()
                graphInfoTail.text = getString(R.string.sr)
                newAdapter.setBaseLineBoolean(false)
                fiftyPercentView.alpha = 0.0F
            }
            1 -> { // Win Rate
                newAdapter.setBaseLineBoolean(true)
                newAdapter.setBase(50F)
                color = ContextCompat.getColor(parent.baseContext, R.color.colorPrimary)
                srGraphButton.alpha = 0.5F
                wrGraphButton.alpha = 1F
                graphInfo.text = "%.2f".format(parent.winRate*100F)
                graphInfoTail.text = "%"
                // Constrains 50% Indicator to baseline
                val base = graph.baseline
                val fiftyPercentId = fiftyPercentView.id
                val constraintSet = ConstraintSet()
                constraintSet.clone(this.view as ConstraintLayout)
                constraintSet.connect(base, ConstraintSet.TOP, fiftyPercentId, ConstraintSet.BOTTOM, 8)
                constraintSet.connect(base, ConstraintSet.LEFT, fiftyPercentId, ConstraintSet.LEFT, 0)
                constraintSet.applyTo(this.view as ConstraintLayout)
                fiftyPercentView.alpha = 1.0F
            }
        }
        newAdapter.setY(floatArray)
        graph.lineColor = color
        graph.scrubLineColor = color
        graphInfo.setTextColor(color)
        graphInfoTail.setTextColor(color)
        graph.adapter = newAdapter
    }

    private fun addValue(tab: Int, it: DataSnapshot): Float {
        return if (tab == 0) it.child("sr").value.toString().toFloat()
        else it.child("winRate").value.toString().toFloat()*100F
    }
}