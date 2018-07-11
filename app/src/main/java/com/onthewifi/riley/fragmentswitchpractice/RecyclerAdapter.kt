package com.onthewifi.riley.fragmentswitchpractice

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.google.firebase.database.DataSnapshot
import kotlinx.android.synthetic.main.tracker_list_item.view.*

class RecyclerAdapter(private var games : ArrayList<DataSnapshot>, val context: Context) : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {
    init { setHasStableIds(true) }


    class ViewHolder (val gameView: ConstraintLayout) : RecyclerView.ViewHolder(gameView) {
        // Holds the views for each game
        val matchNumber: TextView = gameView.findViewById(R.id.matchNumber)
        val srDiff: TextView = gameView.findViewById(R.id.srDiff)
        val mapName: TextView = gameView.findViewById(R.id.mapName)
    }

    // Inflates the item views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val newGame = LayoutInflater.from(context).inflate(R.layout.tracker_list_item, parent, false) as ConstraintLayout
        return ViewHolder(newGame)
    }
    // Binds each game in the ArrayList to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val index = games.size-position-1
        holder.gameView.matchNumber.text = (index+1).toString()
        if (index>0) {
            val diff = games[index].child("sr").value as Long - games[index-1].child("sr").value as Long
            var srString: String = diff.toString() + " SR"
            if(games[index].child("win").value.toString() == "1") {
                srString = "+" + diff.toString() + " SR"
                holder.gameView.srDiff.setTextColor(ContextCompat.getColor(context, R.color.positive))
            } else if (games[index].child("win").value.toString() == "-1") {
                holder.gameView.srDiff.setTextColor(ContextCompat.getColor(context,R.color.negative))
            }
            holder.gameView.srDiff.text = srString

        }
        holder.gameView.mapName.text = games[index].child("map").value as String
    }
    // Gets number of games in list
    override fun getItemCount(): Int {
        return games.size
    }

}

