package com.onthewifi.riley.fragmentswitchpractice

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class FragmentTracker: Fragment() {
//    val TAG = "Tracker"
    private lateinit var parent: MainActivity

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    private lateinit var title: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_tracker,container,false)
        parent = activity as MainActivity
        title = view.findViewById(R.id.titleTextView)
        viewManager = LinearLayoutManager(context)
        viewAdapter = RecyclerAdapter(parent.allGameArray, this.context!!)

        recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView).apply {
            layoutManager = viewManager
            adapter = viewAdapter
        }

        return view
    }

}