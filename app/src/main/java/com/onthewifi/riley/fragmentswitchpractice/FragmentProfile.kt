package com.onthewifi.riley.fragmentswitchpractice

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.support.v4.content.res.ResourcesCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import org.w3c.dom.Text

class FragmentProfile: Fragment(), SrInitDialog.OnInputListener {
    private var TAG = "profile"
    private lateinit var parent: MainActivity

    // For sr init
    override fun sendInput(input: Int) {
        userPath.child("sr").setValue(input)
    }

    private lateinit var userPath: DatabaseReference

    private lateinit var title : TextView
    private lateinit var sr : TextView
    private lateinit var heroImage : ImageView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_profile,container,false)
        parent = activity as MainActivity
        userPath = parent.databaseRef.child("users").child(parent.user!!.uid)

        title = view.findViewById(R.id.titleTextView)
        sr = view.findViewById(R.id.srLarge)
        heroImage = view.findViewById(R.id.heroImage)

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
        parent.name = parent.user!!.displayName ?: "User"
        parent.sr = (snap.child("sr").value as Long).toInt()
        title.text = getString(R.string.profile_title).format(parent.name)
        sr.text = parent.sr.toString()

        if (parent.mainHero == null) heroImage.setImageDrawable(ResourcesCompat.getDrawable(resources, Hero.from(parent.mainHero)!!.getDrawable(), null))
        else heroImage.setImageDrawable(ResourcesCompat.getDrawable(resources, R.color.transparent, null))

    }

    fun initSr() {
        val dialog = SrInitDialog() as DialogFragment
        dialog.setTargetFragment(this, 0)
        dialog.show(fragmentManager,"dialog")
    }

}