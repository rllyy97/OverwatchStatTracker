package com.onthewifi.riley.fragmentswitchpractice

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.google.firebase.database.DataSnapshot
import java.text.SimpleDateFormat
import java.util.*

class TrackerRecyclerAdapter(private var games : ArrayList<DataSnapshot>, val context: Context) : RecyclerView.Adapter<TrackerRecyclerAdapter.ViewHolder>() {

    private lateinit var mainActivity: MainActivity
    init {
        setHasStableIds(true)
        if (context is MainActivity) {
            mainActivity = context
        }
    }

    class ViewHolder (val gameView: ConstraintLayout) : RecyclerView.ViewHolder(gameView) {
        // Holds the views for each game
        var timeID: Long = 0
        var gameIndex: Int = 0
        val matchNumber: TextView = gameView.findViewById(R.id.matchNumber)
        val srDiff: TextView = gameView.findViewById(R.id.srDiff)
        val mapName: TextView = gameView.findViewById(R.id.mapName)
        val date: TextView = gameView.findViewById(R.id.date)
        val percentage: TextView = gameView.findViewById(R.id.percent)
    }

    // Inflates the item views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val newGame = LayoutInflater.from(context).inflate(R.layout.tracker_list_item, parent, false) as ConstraintLayout
        return ViewHolder(newGame)
    }
    // Binds each game in the ArrayList to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val index = games.size-position-1
        holder.matchNumber.text = (index+1).toString()
        holder.gameIndex = index
        if (index>0) {
            val diff = games[index].child("sr").value as Long - games[index-1].child("sr").value as Long
            var srString: String = diff.toString() + " SR"
            if(games[index].child("win").value.toString() == "1") {
                srString = "+" + diff.toString() + " SR"
                holder.srDiff.setTextColor(ContextCompat.getColor(context, R.color.positive))
            } else if (games[index].child("win").value.toString() == "-1") {
                holder.srDiff.setTextColor(ContextCompat.getColor(context,R.color.negative))
            }
            holder.srDiff.text = srString

        }
        holder.mapName.text = games[index].child("map").value as String
        holder.timeID = games[index].key!!.toLong()
        holder.date.text = formatDateToString(holder.timeID)
        // Clicking item opens detailed view fragment
        holder.gameView.setOnClickListener {
            fragmentJump(holder)
        }
    }

    // Gets number of games in list
    override fun getItemCount(): Int {
        return games.size
    }

    // handles switching between detail view and list view
    private fun fragmentJump(holder: ViewHolder) {
        val fragment = FragmentMatchDetail()
        val newBundle = Bundle()
        newBundle.putInt("index", holder.gameIndex)
        newBundle.putString("srDiff", holder.srDiff.text.toString())
        fragment.arguments = newBundle
        val newView = ((mainActivity.viewPager.adapter as FragmentAdapter).getItem(2) as FragmentEmptyFrame).baseView.id
        mainActivity.switchContent(fragment, newView, "detail")
    }

    private fun formatDateToString(input: Long): String {
        @SuppressLint("SimpleDateFormat") // Time zone is set below, warning is irrelevant
        val sdf = SimpleDateFormat("EEE, MM/d/yy")
        sdf.timeZone = Calendar.getInstance().timeZone
        return sdf.format(input)
    }

}

