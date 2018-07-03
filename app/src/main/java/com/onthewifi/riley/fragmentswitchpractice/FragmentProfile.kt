package com.onthewifi.riley.fragmentswitchpractice

import android.content.res.Resources
import android.os.Bundle
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
import com.firebase.ui.auth.data.model.Resource
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.robinhood.spark.SparkView
import org.jetbrains.anko.find
import java.util.*

class FragmentProfile: Fragment(), SrInitDialog.OnInputListener {
    private var TAG = "profile"
    private lateinit var parent: MainActivity

    // For sr init
    override fun sendInput(input: Int) {
        userPath.child("sr").setValue(input)
        userPath.child("name").setValue(parent.user!!.displayName)
    }

    private lateinit var userPath: DatabaseReference

    private lateinit var title : TextView
    private lateinit var srView : TextView
    private lateinit var srTail : TextView
    private lateinit var heroImage : ImageView
    private lateinit var graph : SparkView
    private lateinit var winRateView : TextView

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
        srView = view.findViewById(R.id.srLarge)
        srTail = view.findViewById(R.id.srTail)
        heroImage = view.findViewById(R.id.heroImage)
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
        srView.text = parent.sr.toString()

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

    private fun initGraph(tab: Int, span: Int) {
        currentGraphTab = tab
        currentGraphSpan = span
        if (parent.matchCount < 2)
            // Display 'Play more matches to see your stats'
        else {
            val newAdapter = GraphAdapter()
            var color = 0
            val currentTime = Calendar.getInstance().timeInMillis
            when (currentGraphTab) {
                0 -> { // SR
                    val srArray: ArrayList<Float> = ArrayList()
                    parent.latestSnap!!.child("matches").children.forEach {
                        when (currentGraphSpan) {
                            0 -> { // All
                                srArray.add(it.child("sr").value.toString().toFloat())
                                allGraphButton.alpha = 1.0F
                                weeklyGraphButton.alpha = 0.5F
                                dailyGraphButton.alpha = 0.5F
                            }
                            1 -> { // Weekly
                                if(it.key!!.toLong() > (currentTime - 7 * 24 * 60 * 60 * 1000)) {
                                    srArray.add(it.child("sr").value.toString().toFloat())
                                    allGraphButton.alpha = 0.5F
                                    weeklyGraphButton.alpha = 1.0F
                                    dailyGraphButton.alpha = 0.5F
                                }
                            }
                            2 -> { // Daily
                                if(it.key!!.toLong() > (currentTime - 24 * 60 * 60 * 1000)) {
                                    srArray.add(it.child("sr").value.toString().toFloat())
                                    allGraphButton.alpha = 0.5F
                                    weeklyGraphButton.alpha = 0.5F
                                    dailyGraphButton.alpha = 1.0F
                                }
                            }
                        }

                    }
                    newAdapter.setY(srArray)
                    newAdapter.setBaseLineBoolean(false)
                    color = ContextCompat.getColor(parent.baseContext, R.color.colorAccent)
                    wrGraphButton.alpha = 0.5F
                    srGraphButton.alpha = 1F
                    srView.text = parent.sr.toString()
                    srTail.text = getString(R.string.sr)
                }
                1 -> { // Win Rate
                    val wrArray: ArrayList<Float> = ArrayList()
                    parent.latestSnap!!.child("matches").children.forEach {
                        when (currentGraphSpan) {
                            0 -> { // All
                                wrArray.add(it.child("winRate").value.toString().toFloat()*100F)
                                allGraphButton.alpha = 1.0F
                                weeklyGraphButton.alpha = 0.5F
                                dailyGraphButton.alpha = 0.5F
                            }
                            1 -> { // Weekly
                                if(it.key!!.toLong() > (currentTime - 7 * 24 * 60 * 60 * 1000)) {
                                    wrArray.add(it.child("winRate").value.toString().toFloat()*100F)
                                    allGraphButton.alpha = 0.5F
                                    weeklyGraphButton.alpha = 1.0F
                                    dailyGraphButton.alpha = 0.5F
                                }
                            }
                            2 -> { // Daily
                                if(it.key!!.toLong() > (currentTime - 24 * 60 * 60 * 1000)) {
                                    wrArray.add(it.child("winRate").value.toString().toFloat()*100F)
                                    allGraphButton.alpha = 0.5F
                                    weeklyGraphButton.alpha = 0.5F
                                    dailyGraphButton.alpha = 1.0F
                                }
                            }
                        }
                    }
                    newAdapter.setY(wrArray)
                    newAdapter.setBaseLineBoolean(true)
                    newAdapter.setBase(50F)
                    color = ContextCompat.getColor(parent.baseContext, R.color.colorPrimary)
                    srGraphButton.alpha = 0.5F
                    wrGraphButton.alpha = 1F
                    srView.text = "%.2f".format(parent.winRate*100F)
                    srTail.text = "%"
                }
            }
            graph.lineColor = color
            srView.setTextColor(color)
            srTail.setTextColor(color)
            graph.adapter = newAdapter
        }
    }


}