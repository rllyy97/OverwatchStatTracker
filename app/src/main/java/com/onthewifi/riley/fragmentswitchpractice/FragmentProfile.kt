package com.onthewifi.riley.fragmentswitchpractice

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.os.Bundle
import android.support.constraint.ConstraintLayout
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
import com.robinhood.spark.animation.MorphSparkAnimator
import org.jetbrains.anko.find
import java.util.*


class FragmentProfile: Fragment(), SrInitDialog.OnInputListener {
    private var fragmentTag = "profile"
    private lateinit var parent: MainActivity

    // For sr init
    override fun sendInput(input: Int) {
        userPath.child("sr").setValue(input)
        userPath.child("careerHigh").setValue(input)
        userPath.child("name").setValue(parent.user!!.displayName)
    }

    private lateinit var userPath: DatabaseReference
    private lateinit var baseView: ConstraintLayout

    // Current Game Array
    private var filteredGameArray : ArrayList<DataSnapshot> = ArrayList()

    // Header Views
    private lateinit var title : TextView
    private lateinit var graphInfo : TextView
    private lateinit var graphInfoTail : TextView
    private lateinit var backgroundImage : ImageView

    // Graph Views
    private lateinit var graph : SparkView
    private var color: Int = 0
    private lateinit var lowMatchWarning : TextView
    private lateinit var srGraphButton : Button
    private lateinit var wrGraphButton : Button
    private var currentGraphTab : Int = 0
    private lateinit var allGraphButton : Button
    private lateinit var weeklyGraphButton : Button
    private lateinit var dailyGraphButton : Button
    private var currentGraphSpan : Int = 1

    // Footer Views
    private lateinit var careerHigh : TextView
    private lateinit var totalMatches : TextView
    private lateinit var avgKDView : TextView
    private lateinit var avgDamagePerMinView : TextView
    private lateinit var avgHealsPerMinView : TextView
    private lateinit var wdlView : TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_profile,container,false)
        parent = activity as MainActivity
        userPath = parent.databaseRef.child("users").child(parent.user!!.uid)

        baseView = view.findViewById(R.id.baseView)
        title = view.findViewById(R.id.titleTextView)
        graphInfo = view.findViewById(R.id.graphInfo)
        graphInfoTail = view.findViewById(R.id.graphInfoTail)
        backgroundImage = view.findViewById(R.id.backgroundImage)


        graph = view.findViewById(R.id.mainGraph)
        graph.adapter = GraphAdapter()
        (graph.adapter as GraphAdapter).setBase(50F)
        val paint = Paint()
        paint.strokeCap = Paint.Cap.ROUND
        paint.strokeWidth = 3F
        paint.color = Color.GRAY
        paint.style = Paint.Style.STROKE
        paint.pathEffect = DashPathEffect(floatArrayOf(3F, 15F),0F)
        graph.baseLinePaint = paint
        graph.sparkAnimator = MorphSparkAnimator()
        srGraphButton = view.findViewById(R.id.srGraphButton)
        wrGraphButton = view.findViewById(R.id.wrGraphButton)
        allGraphButton = view.findViewById(R.id.allGraphButton)
        weeklyGraphButton = view.find(R.id.weeklyGraphButton)
        dailyGraphButton = view.findViewById(R.id.dailyGraphButton)
        lowMatchWarning = view.findViewById(R.id.lowMatchWarning)

        careerHigh = view.findViewById(R.id.careerHigh)
        totalMatches = view.findViewById(R.id.totalMatches)
        avgKDView = view.findViewById(R.id.avgKD)
        avgDamagePerMinView = view.findViewById(R.id.avgDamagePerMin)
        avgHealsPerMinView = view.findViewById(R.id.avgHealsperMin)
        wdlView = view.findViewById(R.id.wdl)

        srGraphButton.setOnClickListener { updateGraph(0, currentGraphSpan) }
        wrGraphButton.setOnClickListener { updateGraph(1, currentGraphSpan) }

        allGraphButton.setOnClickListener { updateGraph(currentGraphTab, 0) }
        weeklyGraphButton.setOnClickListener { updateGraph(currentGraphTab, 1) }
        dailyGraphButton.setOnClickListener { updateGraph(currentGraphTab, 2) }

        initialize()

        return view
    }

    private fun initialize() {
        userPath.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snap: DataSnapshot) {
                if (snap.child("sr").value != null) {
                    if(snap.child("matches").childrenCount.toInt() != 0)
                        loadData(snap)
                }
                else initSr()
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "whoops", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun loadData(snap: DataSnapshot) {
        parent.latestSnap = snap
        parent.refreshGameArray()
        parent.sr = (snap.child("sr").value as Long).toInt()
        parent.winRate = (snap.child("winRate").value as Number).toFloat()
        parent.careerHigh = (snap.child("careerHigh").value as Long).toInt()
        parent.matchCount = (snap.child("matches").childrenCount).toInt()
        if (context != null) loadUI()
    }

    private fun loadUI() {
        parent.name = parent.user!!.displayName ?: "User"
        if(parent.name.last() != 's') title.text = getString(R.string.profile_title).format(parent.name)
        else title.text = getString(R.string.profile_title_s).format(parent.name)
        updateRankingImage(parent.sr)
        updateGraph(currentGraphTab, currentGraphSpan)
        updateData()
    }

    fun initSr() {
        val dialog = SrInitDialog() as DialogFragment
        dialog.setTargetFragment(this, 0)
        dialog.show(fragmentManager,"dialog")
    }

    @SuppressLint("SetTextI18n")
    private fun updateGraph(tab: Int, span: Int) {
        currentGraphTab = tab
        currentGraphSpan = span

        val currentTime = Calendar.getInstance().timeInMillis
        filteredGameArray.clear()
        val floatArray: ArrayList<Float> = ArrayList()
        parent.latestSnap!!.child("matches").children.forEach {
            when (currentGraphSpan) {
                0 -> { // All
                    filteredGameArray.add(it)
                    floatArray.add(addValue(currentGraphTab, it))
                    selectButton(allGraphButton, 0)
                    unselectButton(weeklyGraphButton)
                    unselectButton(dailyGraphButton)
                }
                1 -> { // Weekly
                    if(it.key!!.toLong() > (currentTime - 7 * 24 * 60 * 60 * 1000)) {
                        filteredGameArray.add(it)
                        floatArray.add(addValue(currentGraphTab, it))
                    }
                    unselectButton(allGraphButton)
                    selectButton(weeklyGraphButton, 0)
                    unselectButton(dailyGraphButton)
                }
                2 -> { // Daily
                    if(it.key!!.toLong() > (currentTime - 24 * 60 * 60 * 1000)) {
                        filteredGameArray.add(it)
                        floatArray.add(addValue(currentGraphTab, it))
                    }
                    unselectButton(allGraphButton)
                    unselectButton(weeklyGraphButton)
                    selectButton(dailyGraphButton, 0)
                }
            }
        }

        // Calls new match average loader
        ((parent.viewPager.adapter as FragmentAdapter).getItem(1) as FragmentNewMatch).loadAverages()

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
                    0 -> graphInfo.text = (it as Float).toInt().toString()
                    1 -> graphInfo.text =  "%.1f".format(it)
                }
            } else {
                when (currentGraphTab) {
                    0 -> graphInfo.text = parent.sr.toString()
                    1 -> graphInfo.text = "%.1f".format(parent.winRate*100F)
                }
            }
        }

        var valueAnimator = ValueAnimator()
        when (currentGraphTab) {
            0 -> { // SR
                valueAnimator = ValueAnimator.ofArgb(color, ContextCompat.getColor(parent.baseContext, R.color.colorAccent))
                (graph.adapter as GraphAdapter).setBaseLineBoolean(false)
                unselectButton(wrGraphButton)
                selectButton(srGraphButton, 1)
                graphInfo.text = parent.sr.toString()
                graphInfoTail.text = getString(R.string.sr)

            }
            1 -> { // Win Rate
                valueAnimator = ValueAnimator.ofArgb(color, ContextCompat.getColor(parent.baseContext, R.color.colorPrimary))
                (graph.adapter as GraphAdapter).setBaseLineBoolean(true)
                selectButton(wrGraphButton, 2)
                unselectButton(srGraphButton)
                graphInfo.text = "%.1f".format(parent.winRate*100F)
                graphInfoTail.text = "%"
            }
        }

        (graph.adapter as GraphAdapter).initY(floatArray)
        graph.adapter.notifyDataSetChanged()
        valueAnimator.addUpdateListener {
            color = it.animatedValue as Int
            graph.lineColor = color
            graphInfo.setTextColor(color)
            graphInfoTail.setTextColor(color)
            graph.scrubLineColor = color
        }
        valueAnimator.start()

    }

    @SuppressLint("SetTextI18n")
    private fun updateData() {
        val averages = parent.profileAverages

        avgKDView.text = "%.2f".format(averages.avgEliminationsDeath)
        avgDamagePerMinView.text = "%.0f".format(averages.avgDamageMin)
        avgHealsPerMinView.text = "%.0f".format(averages.avgHealingMin)

        graphInfo.text = parent.sr.toString()
        careerHigh.text = parent.careerHigh.toString()
        totalMatches.text = filteredGameArray.size.toString()

        var runningWins = 0
        var runningDraws = 0
        var runningLosses = 0

        for (game in filteredGameArray) {
            if(game.child("win").exists()){
                when ((game.child("win").value as Long).toInt()) {
                    1 -> runningWins++
                    0 -> runningDraws++
                    -1 -> runningLosses++
                }
            }
        }

        val wdlString = "%d - %d - %d".format(runningWins,runningDraws,runningLosses)
        wdlView.text = wdlString
    }

    // Helper function to return sr or win rate of a match
    private fun addValue(tab: Int, it: DataSnapshot): Float {
        return if (tab == 0) it.child("sr").value.toString().toFloat()
        else it.child("winRate").value.toString().toFloat()*100F
    }

    // Sets the background image to the current SR rank
    private fun updateRankingImage(rank: Int) {
        when (rank) {
            in 1..1499 -> backgroundImage.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.bronze, null))
            in 1500..1999 -> backgroundImage.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.silver, null))
            in 2000..2499 -> backgroundImage.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.gold, null))
            in 2500..2999 -> backgroundImage.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.platinum, null))
            in 3000..3499 -> backgroundImage.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.diamond, null))
            in 3500..3999 -> backgroundImage.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.masters, null))
            in 4000..5000 -> backgroundImage.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.grandmasters, null))
        }
    }

    // Sets Button to selected
    private fun selectButton(button: Button, color: Int) {
        when (color) {
            0 -> { // dates - grey
                button.backgroundTintList = ResourcesCompat.getColorStateList(resources, R.color.buttonSelected, null)
                button.setTextColor(ResourcesCompat.getColorStateList(resources, R.color.buttonSelectedText, null))
            }
            1 -> { // SR - accent
                button.backgroundTintList = ResourcesCompat.getColorStateList(resources, R.color.colorAccentLight, null)
                button.setTextColor(ResourcesCompat.getColorStateList(resources, R.color.colorAccentDark, null))
            }
            2 -> { // WR - primary
                button.backgroundTintList = ResourcesCompat.getColorStateList(resources, R.color.colorPrimaryLight, null)
                button.setTextColor(ResourcesCompat.getColorStateList(resources, R.color.colorPrimaryDark, null))
            }
        }
    }

    // Sets Button to unselected
    private fun unselectButton(button: Button) {
        button.backgroundTintList = ResourcesCompat.getColorStateList(resources, R.color.buttonUnselected, null)
        button.setTextColor(ResourcesCompat.getColorStateList(resources, R.color.buttonUnselectedText, null))
    }

}