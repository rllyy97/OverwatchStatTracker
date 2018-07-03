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
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.firebase.ui.auth.data.model.Resource
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.robinhood.spark.SparkView

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
    private lateinit var heroImage : ImageView
    private lateinit var graph : SparkView
    private lateinit var winRateView : TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_profile,container,false)
        parent = activity as MainActivity
        userPath = parent.databaseRef.child("users").child(parent.user!!.uid)

        title = view.findViewById(R.id.titleTextView)
        srView = view.findViewById(R.id.srLarge)
        heroImage = view.findViewById(R.id.heroImage)
        graph = view.findViewById(R.id.mainGraph)

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

        graphInit(0)

    }

    fun initSr() {
        val dialog = SrInitDialog() as DialogFragment
        dialog.setTargetFragment(this, 0)
        dialog.show(fragmentManager,"dialog")
    }

    // Graph functions

    private fun graphInit(tab: Int) {
        if (parent.matchCount < 2)
            // Display 'Play more matches to see your stats'
        else {
            val newAdapter = GraphAdapter()
            when (tab) {
                0 -> { // SR
                    val srArray: ArrayList<Float> = ArrayList()
                    parent.latestSnap!!.child("matches").children.forEach {
                        srArray.add(it.child("sr").value.toString().toFloat())
                    }
                    newAdapter.setY(srArray)
                    newAdapter.setBaseLineBoolean(false)
                }
                1 -> { // Win Rate
                    val wrArray: ArrayList<Float> = ArrayList()
                    parent.latestSnap!!.child("matches").children.forEach {
                        wrArray.add(it.child("winRate").value.toString().toFloat() * 100F)
                    }
                    newAdapter.setY(wrArray)
                    newAdapter.setBaseLineBoolean(true)
                    newAdapter.setBase(50F)

                }
            }
            graph.adapter = newAdapter
        }
    }


}