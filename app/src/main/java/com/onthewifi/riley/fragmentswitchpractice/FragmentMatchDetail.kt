package com.onthewifi.riley.fragmentswitchpractice

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.content.res.ResourcesCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.google.firebase.database.DataSnapshot
import java.text.SimpleDateFormat
import java.util.*

class FragmentMatchDetail: Fragment() {
    private var fragmentTag = "match_detail"
    private lateinit var parent: MainActivity
    private lateinit var view: ConstraintLayout
    private var gameID: Long = 0
    private var gameIndex: Int = 0
    private lateinit var gameSnap: DataSnapshot

    // all match data and percentages
    private lateinit var dataChunk: HeroMatchData

    // UI Elements
    private lateinit var deleteMatchButton: Button
    private lateinit var backButton: ImageView

    private lateinit var dateView: TextView
    private lateinit var mapView: TextView
    private lateinit var mapIcon: ImageView
    private lateinit var srDiffView: TextView

    private lateinit var heroContainer: LinearLayout

    private lateinit var damageLayout: ConstraintLayout
    private lateinit var damageTotalView: TextView
    private lateinit var damageMinView: TextView
    private lateinit var damageDeathView: TextView
    private lateinit var damageMinPercentView: TextView
    private lateinit var damageDeathPercentView: TextView

    private lateinit var healingLayout: ConstraintLayout
    private lateinit var healingTotalView: TextView
    private lateinit var healingMinView: TextView
    private lateinit var healingDeathView: TextView
    private lateinit var healingMinPercentView: TextView
    private lateinit var healingDeathPercentView: TextView

    private lateinit var elimsLayout: ConstraintLayout
    private lateinit var elimsTotalView: TextView
    private lateinit var elimsMinView: TextView
    private lateinit var elimsDeathView: TextView
    private lateinit var elimsMinPercentView: TextView
    private lateinit var elimsDeathPercentView: TextView

    private lateinit var deathsLayout: ConstraintLayout
    private lateinit var deathsTotalView: TextView
    private lateinit var deathsMinView: TextView
    private lateinit var deathsMinPercentView: TextView

    private lateinit var accuracyView: TextView
    private lateinit var accuracyPercentView: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        view = inflater.inflate(R.layout.fragment_match_detail,container,false) as ConstraintLayout
        parent = activity as MainActivity
        gameIndex = this.arguments!!.getInt("index")
        gameSnap = parent.allGameArray[gameIndex]
        gameID = gameSnap.key!!.toLong()

        // UI initialization
        deleteMatchButton = view.findViewById(R.id.deleteMatchButton)
        backButton = view.findViewById(R.id.backButton)
        dateView = view.findViewById(R.id.date)
        mapView = view.findViewById(R.id.map)
        mapIcon = view.findViewById(R.id.mapIcon)
        srDiffView = view.findViewById(R.id.srDiff)
        heroContainer = view.findViewById(R.id.heroContainer)

        damageLayout = view.findViewById(R.id.damageLayout)
        damageTotalView = view.findViewById(R.id.damageTotal)
        damageMinView = view.findViewById(R.id.damagePerMin)
        damageDeathView = view.findViewById(R.id.damagePerDeath)
        damageMinPercentView = view.findViewById(R.id.damagePerMinPercent)
        damageDeathPercentView = view.findViewById(R.id.damagePerDeathPercent)

        healingLayout = view.findViewById(R.id.healingLayout)
        healingTotalView = view.findViewById(R.id.healingTotal)
        healingMinView = view.findViewById(R.id.healingPerMin)
        healingDeathView = view.findViewById(R.id.healingPerDeath)
        healingMinPercentView = view.findViewById(R.id.healingPerMinPercent)
        healingDeathPercentView = view.findViewById(R.id.healingPerDeathPercent)

        elimsLayout = view.findViewById(R.id.elimsLayout)
        elimsTotalView = view.findViewById(R.id.elimsTotal)
        elimsMinView = view.findViewById(R.id.elimsPerMin)
        elimsDeathView = view.findViewById(R.id.elimsPerDeath)
        elimsMinPercentView = view.findViewById(R.id.elimsPerMinPercent)
        elimsDeathPercentView = view.findViewById(R.id.elimsPerDeathPercent)

        deathsLayout = view.findViewById(R.id.elimsLayout)
        deathsTotalView = view.findViewById(R.id.deathsTotal)
        deathsMinView = view.findViewById(R.id.deathsPerMin)
        deathsMinPercentView = view.findViewById(R.id.deathsPerMinPercent)

        accuracyView = view.findViewById(R.id.accuracy)
        accuracyPercentView = view.findViewById(R.id.accuracyPercent)

        backButton.setOnClickListener { finish() }
        deleteMatchButton.setOnClickListener{
            // Toast deletion
            // Fix win count and win rate
            parent.databaseRef.child("users").child(parent.user!!.uid).child("matches").child(gameID.toString()).removeValue()
            finish()
        }

        loadData()
        loadUI()

        return view
    }

    private fun finish() {
        fragmentManager!!.popBackStack()
    }

    @SuppressLint("SetTextI18n")
    private fun loadUI() {
        @SuppressLint("SimpleDateFormat") // Time zone is set below, warning is irrelevant
        val sdf = SimpleDateFormat("EEEE, MMMM d, yyyy - KK:mm aa")
        sdf.timeZone = Calendar.getInstance().timeZone
        dateView.text = sdf.format(gameID)

        mapView.text = gameSnap.child("map").value.toString()
        val mapCategory = Map.from(gameSnap.child("map").value.toString())!!.getType()
        when (mapCategory) {
            "assault"     -> mapIcon.setImageDrawable(ContextCompat.getDrawable(context!!, R.drawable.assault))
            "escort"      -> mapIcon.setImageDrawable(ContextCompat.getDrawable(context!!, R.drawable.escort))
            "combination" -> mapIcon.setImageDrawable(ContextCompat.getDrawable(context!!, R.drawable.combination))
            "control"     -> mapIcon.setImageDrawable(ContextCompat.getDrawable(context!!, R.drawable.control))
        }

        srDiffView.text = this.arguments!!.getString("srDiff")
        when {
            srDiffView.text[0] == '+' -> srDiffView.setBackgroundColor(ContextCompat.getColor(context!!, R.color.positive))
            srDiffView.text[0] == '-' -> srDiffView.setBackgroundColor(ContextCompat.getColor(context!!, R.color.negative))
            else                      -> srDiffView.setBackgroundColor(ContextCompat.getColor(context!!, R.color.colorDark))
        }

        for (hero in gameSnap.child("heroes").children) {
            addCharacter(hero.value as String)
        }

        damageTotalView.text = "%.0f".format(dataChunk.damage)
        damageMinView.text = "%.0f".format(dataChunk.damageMin)
        damageDeathView.text = "%.0f".format(dataChunk.damageDeath)
        formatPercentView(damageMinPercentView, dataChunk.damageMinPercent)
        formatPercentView(damageDeathPercentView, dataChunk.damageDeathPercent)

        if (dataChunk.healing == 0f && dataChunk.healingMinPercent.isNaN())
            healingLayout.visibility = View.GONE
        else {
            healingTotalView.text = "%.0f".format(dataChunk.healing)
            healingMinView.text = "%.0f".format(dataChunk.healingMin)
            healingDeathView.text = "%.0f".format(dataChunk.healingDeath)
            formatPercentView(healingMinPercentView, dataChunk.healingMinPercent)
            formatPercentView(healingDeathPercentView, dataChunk.healingDeathPercent)
        }

        elimsTotalView.text = "%.0f".format(dataChunk.elims)
        elimsMinView.text = "%.2f".format(dataChunk.elimsMin)
        elimsDeathView.text = "%.2f".format(dataChunk.elimsDeath)
        formatPercentView(elimsMinPercentView, dataChunk.elimsMinPercent)
        formatPercentView(elimsDeathPercentView, dataChunk.elimsDeathPercent)

        deathsTotalView.text = "%.0f".format(dataChunk.deaths)
        deathsMinView.text = "%.2f".format(dataChunk.deathsMin)
        formatPercentView(deathsMinPercentView, dataChunk.deathsMinPercent)
        if (deathsMinPercentView.text[0] == '+') deathsMinPercentView.setTextColor(ResourcesCompat.getColor(resources, R.color.negative, null))
        if (deathsMinPercentView.text[0] == '-') deathsMinPercentView.setTextColor(ResourcesCompat.getColor(resources, R.color.positive, null))

        val accString = dataChunk.accuracy.toString() + "%"
        accuracyView.text = accString
        formatPercentView(accuracyPercentView, dataChunk.accuracyPercent)

    }

    private fun addCharacter(heroString: String) {
        val heroImage = ImageView(context)
        val scale = context!!.resources.displayMetrics.density
        val width = (70 * scale + 0.5f).toInt()
        heroImage.layoutParams = ViewGroup.LayoutParams(width, ViewGroup.LayoutParams.MATCH_PARENT)
        heroImage.setImageDrawable(ResourcesCompat.getDrawable(resources, Hero.from(heroString)!!.getDrawable(), null))
        heroImage.adjustViewBounds = true
        heroContainer.addView(heroImage)
    }

    private fun loadData() {
        dataChunk = parent.focusedMatchData
    }

    private fun formatPercentView(view: TextView, input: Float) {
        var string = "-"
        when {
            input > 0f -> {
                view.setTextColor(ResourcesCompat.getColor(resources, R.color.positive, null))
                string = "+%.2f%%".format(input*100)
            }
            input < 0f -> {
                view.setTextColor(ResourcesCompat.getColor(resources, R.color.negative, null))
                string = "%.2f%%".format(input*100)
            }
        }
        view.text = string
    }

}