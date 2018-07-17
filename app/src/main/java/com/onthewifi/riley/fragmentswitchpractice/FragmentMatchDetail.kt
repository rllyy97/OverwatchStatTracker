package com.onthewifi.riley.fragmentswitchpractice

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.RecyclerView
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

    // Match Stats
    private var damage = 0f
    private var damageMin = 0f
    private var damageDeath = 0f
    private var damageMinPercent = 0f
    private var damageDeathPercent = 0f
    private var healing = 0f
    private var healingMin = 0f
    private var healingDeath = 0f
    private var healingMinPercent = 0f
    private var healingDeathPercent = 0f
    private var elims = 0f
    private var elimsMin = 0f
    private var elimsDeath = 0f
    private var elimsMinPercent = 0f
    private var elimsDeathPercent = 0f
    private var deaths = 0f
    private var deathsMin = 0f
    private var deathsDeaths = 0f // this is redundant but it makes me laugh so it stays
    private var deathsMinPercent = 0f
    private var deathsDeathsPercent = 0f // this is also redundant
    private var length = 0f
    private var accuracy = 0f
    private var accuracyPercent = 0f
    private var totalPercent = 0f

    // Averages to get percentages
    private var avgDamageMin = 0f
    private var avgHealingMin = 0f
    private var avgEliminationsMin = 0f
    private var avgDeathsMin = 0f
    private var avgDamageDeath = 0f
    private var avgHealingDeath = 0f
    private var avgEliminationsDeath = 0f
    private var avgAccuracy = 0f

    // UI Elements
    private lateinit var deleteMatchButton: Button
    private lateinit var backButton: ImageView

    private lateinit var dateView: TextView
    private lateinit var mapView: TextView
    private lateinit var mapIcon: ImageView
    private lateinit var srDiffView: TextView

    private lateinit var heroContainer: LinearLayout

    private lateinit var damageTotalView: TextView
    private lateinit var damageMinView: TextView
    private lateinit var damageDeathView: TextView
    private lateinit var damageMinPercentView: TextView
    private lateinit var damageDeathPercentView: TextView

    private lateinit var healingTotalView: TextView
    private lateinit var healingMinView: TextView
    private lateinit var healingDeathView: TextView
    private lateinit var healingMinPercentView: TextView
    private lateinit var healingDeathPercentView: TextView

    private lateinit var elimsTotalView: TextView
    private lateinit var elimsMinView: TextView
    private lateinit var elimsDeathView: TextView
    private lateinit var elimsMinPercentView: TextView
    private lateinit var elimsDeathPercentView: TextView

    private lateinit var deathsTotalView: TextView
    private lateinit var deathsMinView: TextView
    private lateinit var deathsMinPercentView: TextView


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

        damageTotalView = view.findViewById(R.id.damageTotal)
        damageMinView = view.findViewById(R.id.damagePerMin)
        damageDeathView = view.findViewById(R.id.damagePerDeath)
        damageMinPercentView = view.findViewById(R.id.damagePerMinPercent)
        damageDeathPercentView = view.findViewById(R.id.damagePerDeathPercent)

        healingTotalView = view.findViewById(R.id.healingTotal)
        healingMinView = view.findViewById(R.id.healingPerMin)
        healingDeathView = view.findViewById(R.id.healingPerDeath)
        healingMinPercentView = view.findViewById(R.id.healingPerMinPercent)
        healingDeathPercentView = view.findViewById(R.id.healingPerDeathPercent)

        elimsTotalView = view.findViewById(R.id.elimsTotal)
        elimsMinView = view.findViewById(R.id.elimsPerMin)
        elimsDeathView = view.findViewById(R.id.elimsPerDeath)
        elimsMinPercentView = view.findViewById(R.id.elimsPerMinPercent)
        elimsDeathPercentView = view.findViewById(R.id.elimsPerDeathPercent)

        deathsTotalView = view.findViewById(R.id.deathsTotal)
        deathsMinView = view.findViewById(R.id.deathsPerMin)
        deathsMinPercentView = view.findViewById(R.id.deathsPerMinPercent)

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

        damageTotalView.text = damage.toString()
        damageMinView.text = damageMin.toString()
        damageDeathView.text = damageDeath.toString()
        formatPercentView(damageMinPercentView, damageMinPercent)
        formatPercentView(damageDeathPercentView, damageDeathPercent)

        healingTotalView.text = healing.toString()
        healingMinView.text = healingMin.toString()
        healingDeathView.text = healingDeath.toString()
        formatPercentView(healingMinPercentView, healingMinPercent)
        formatPercentView(healingDeathPercentView, healingDeathPercent)

        elimsTotalView.text = elims.toString()
        elimsMinView.text = elimsMin.toString()
        elimsDeathView.text = elimsDeath.toString()
        formatPercentView(elimsMinPercentView, elimsMinPercent)
        formatPercentView(elimsDeathPercentView, elimsDeathPercent)

        deathsTotalView.text = deaths.toString()
        deathsMinView.text = deathsMin.toString()
        formatPercentView(deathsMinPercentView, deathsMinPercent)

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

    private fun loadAverages() {
        avgDamageMin = parent.avgDamageMin
        avgHealingMin = parent.avgHealingMin
        avgEliminationsMin = parent.avgEliminationsMin
        avgDeathsMin = parent.avgDeathsMin
        avgDamageDeath = parent.avgDamageDeath
        avgHealingDeath = parent.avgHealingDeath
        avgEliminationsDeath = parent.avgEliminationsDeath
        avgAccuracy = parent.avgAccuracy
    }

    private fun loadData() {
        loadAverages()
        length = getDataItem("length")
        accuracy = getDataItem("accuracy")
        damage = getDataItem("damage")
        healing = getDataItem("heals")
        elims = getDataItem("eliminations")
        deaths = getDataItem("deaths")

        damageMin = damage / length
        healingMin = healing / length
        elimsMin = elims / length
        deathsMin = deaths / length
        damageDeath = damage / deaths
        healingDeath = healing / deaths
        elimsDeath = elims / deaths

        damageMinPercent = (damageMin / avgDamageMin) - 1
        healingMinPercent = (healingMin / avgHealingMin) - 1
        elimsMinPercent = (elimsMin / avgEliminationsMin) - 1
        deathsMinPercent = (deathsMin / avgDeathsMin) - 1
        damageDeathPercent = (damageDeath / avgDamageDeath) - 1
        healingDeathPercent = (healingDeath / avgHealingDeath) - 1
        elimsDeathPercent = (elimsDeath / avgEliminationsDeath) - 1
        accuracyPercent = (accuracy / avgAccuracy) - 1

    }

    private fun getDataItem(key: String): Float {
        return (gameSnap.child(key).value as Long).toFloat()
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