package com.onthewifi.riley.fragmentswitchpractice

import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView


class FragmentTracker: Fragment() {
    val fragmentTag = "tracker"
    private lateinit var parent: MainActivity

    private lateinit var view: ConstraintLayout
    lateinit var recyclerView: RecyclerView
    private lateinit var title: TextView


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        view = inflater.inflate(R.layout.fragment_tracker,container,false) as ConstraintLayout
        parent = activity as MainActivity
        title = view.findViewById(R.id.titleTextView)
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        recyclerView.adapter = TrackerRecyclerAdapter(parent.allGameArray, this.context!!)
        return view
    }


}