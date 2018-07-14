package com.onthewifi.riley.fragmentswitchpractice

import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
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

    // UI Elements
    private lateinit var deleteMatchButton: Button
    private lateinit var backButton: ImageView

    private lateinit var dateView: TextView
    private lateinit var mapView: TextView
    private lateinit var mapIcon: ImageView
    private lateinit var srDiffView: TextView

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

        backButton.setOnClickListener { finish() }
        deleteMatchButton.setOnClickListener{
            // Toast deletion
            // Delete Match from database
            finish()
        }

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
            else                      -> srDiffView.setBackgroundColor(ContextCompat.getColor(context!!, R.color.colorPrimaryDark))
        }

        return view
    }

    private fun finish() {
        fragmentManager!!.popBackStack()
    }

}