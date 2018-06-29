package com.onthewifi.riley.fragmentswitchpractice

import android.media.Image
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowId
import android.widget.*
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_new_match.*
import org.jetbrains.anko.find

class FragmentNewMatch: Fragment(), CharacterSelectorDialog.OnInputListener{
    private var TAG = "new_match"
    private lateinit var parent: MainActivity

    override fun sendInput(input: String) {
        heroStringArray[heroCounter] = input
        heroIconArray[heroCounter].setOnClickListener(deleteCharacter)
        heroIconArray[heroCounter].setImageResource(Hero.from(input)!!.getDrawable())

        heroCounter++
        if (heroCounter < 5) {
            heroIconArray[heroCounter].isClickable = true
            heroIconArray[heroCounter].setImageResource(R.drawable.ic_add_black_24dp)
        }

    }

    private var heroCounter = 0
    private var hero1: String = ""
    private var hero2: String = ""
    private var hero3: String = ""
    private var hero4: String = ""
    private var hero5: String = ""
    private var heroStringArray = arrayOf(hero1,hero2,hero3,hero4,hero5)

    // View References
    private lateinit var title: TextView
    private lateinit var hero1icon: ImageButton
    private lateinit var hero2icon: ImageButton
    private lateinit var hero3icon: ImageButton
    private lateinit var hero4icon: ImageButton
    private lateinit var hero5icon: ImageButton
    private lateinit var heroIconArray: Array<ImageButton>
    private lateinit var mapSpinner: Spinner

    private lateinit var eliminations: EditText
    private lateinit var deaths: EditText
    private lateinit var damage: EditText
    private lateinit var heals: EditText
    private lateinit var length: EditText
    private lateinit var accuracy: EditText
    private lateinit var sr: EditText

    private lateinit var clearButton: FloatingActionButton
    private lateinit var submitButton: FloatingActionButton

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_new_match,container,false)
        title = view.findViewById(R.id.titleTextView)
        hero1icon = view.findViewById(R.id.hero1icon)
        hero1icon.tag = 0
        hero2icon = view.findViewById(R.id.hero2icon)
        hero2icon.tag = 1
        hero3icon = view.findViewById(R.id.hero3icon)
        hero3icon.tag = 2
        hero4icon = view.findViewById(R.id.hero4icon)
        hero4icon.tag = 3
        hero5icon = view.findViewById(R.id.hero5icon)
        hero5icon.tag = 4

        mapSpinner = view.findViewById(R.id.mapSpinner)
        mapSpinner.adapter = ArrayAdapter<Map>(context, android.R.layout.simple_dropdown_item_1line, Map.values())

        eliminations = view.findViewById(R.id.eliminations)
        deaths = view.findViewById(R.id.deaths)
        damage = view.findViewById(R.id.damage)
        heals = view.findViewById(R.id.healing)
        length = view.findViewById(R.id.length)
        accuracy = view.findViewById(R.id.accuracy)
        sr = view.findViewById(R.id.sr)

        clearButton = view.findViewById(R.id.clearButton)
        clearButton.setOnClickListener(clearFields)

        submitButton = view.findViewById(R.id.submitButton)
        submitButton.setOnClickListener(submitMatch)

        parent = activity as MainActivity

        heroIconArray = arrayOf(hero1icon, hero2icon, hero3icon, hero4icon, hero5icon)
        for (icon in heroIconArray) {
            icon.setOnClickListener(addCharacter)
        }
        return view
    }

    // Deletes character, cascades others down
    private var deleteCharacter = View.OnClickListener {
        deleteCharacter(it as ImageButton)
    }

    private fun deleteCharacter(view: ImageButton) {
        var marker = view.tag as Int
        while (marker+1 < heroCounter) {
            heroStringArray[marker] = heroStringArray[marker+1]
            heroIconArray[marker].setImageResource(Hero.from(heroStringArray[marker]!!)!!.getDrawable())
            marker++
        }
        heroStringArray[marker] = ""
        heroIconArray[marker].setOnClickListener(addCharacter)
        heroIconArray[marker].setImageResource(R.drawable.ic_add_black_24dp)
        if (heroCounter < 5) {
            marker++
            heroIconArray[marker].isClickable = false
            heroIconArray[marker].setImageResource(R.color.transparent)
        }
        heroCounter--
    }

    // Adds character
    private var addCharacter = View.OnClickListener {
        val dialog = CharacterSelectorDialog() as DialogFragment
        dialog.setTargetFragment(this, 0)
        dialog.show(fragmentManager,"dialog")
    }

    // Clears fragment by restarting it
    private var clearFields = View.OnClickListener {
        clearFields()
    }

    private fun clearFields() {
        while (heroCounter != 0) {
            deleteCharacter(hero1icon)
        }
        eliminations.text.clear()
        deaths.text.clear()
        damage.text.clear()
        heals.text.clear()
        length.text.clear()
        accuracy.text.clear()
        sr.text.clear()
    }

    // Submits data to database
    private var submitMatch: View.OnClickListener = View.OnClickListener {
        submitMatch()
    }

    private fun submitMatch() {
        // Checks fields are full
        if (
            heroStringArray[0] == ""
            || eliminations.text.isEmpty()
            || damage.text.isEmpty()
            || heals.text.isEmpty()
            || deaths.text.isEmpty()
            || accuracy.text.isEmpty()
            || length.text.isEmpty()
            || sr.text.isEmpty()
                ) {
            Toast.makeText(context, "Fill all fields", Toast.LENGTH_SHORT).show()
            return
        }
        // Stores user path in database
        val userPath = parent.databaseRef.child("users").child(parent.user!!.uid)
        // Submits Match
        val match = Match(
                sr.text.toString().toInt(),
                mapSpinner.selectedItem.toString(),
                heroStringArray[0],
                heroStringArray[1],
                heroStringArray[2],
                heroStringArray[3],
                heroStringArray[4],
                eliminations.text.toString().toInt(),
                damage.text.toString().toInt(),
                heals.text.toString().toInt(),
                deaths.text.toString().toInt(),
                accuracy.text.toString().toInt(),
                length.text.toString().toInt())
        val matchKey = userPath.child("matches").push().key
        match.uuid = matchKey!!.toString()
        userPath.child("matches").child(matchKey).setValue(match)
        // Records User Info
        userPath.child("sr").setValue(sr.text.toString().toInt())
        userPath.child("name").setValue(parent.user!!.displayName)

        var matchCount: Int
        userPath.child("matchCount").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snap: DataSnapshot) {
                if(snap.value == null) {
                    matchCount = 1
                } else {
                    matchCount = (snap.value as Long).toInt()
                    matchCount++
                }
                userPath.child("matchCount").setValue(matchCount)
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "whoops", Toast.LENGTH_SHORT).show()
            }
        })

        clearFields()

        Toast.makeText(context, "Submitted Match", Toast.LENGTH_SHORT).show()

    }
}