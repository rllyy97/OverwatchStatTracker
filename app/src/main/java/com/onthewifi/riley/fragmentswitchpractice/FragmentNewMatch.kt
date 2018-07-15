package com.onthewifi.riley.fragmentswitchpractice

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import java.util.*
import kotlin.collections.ArrayList


class FragmentNewMatch: Fragment(), CharacterSelectorDialog.OnInputListener {
    private var fragmentTag = "new_match"
    private lateinit var parent: MainActivity

    // For character picker
    override fun sendInput(input: String) {
        heroStringArray.add(input)
        heroIconArray[heroCounter].setOnClickListener(deleteCharacter)
        heroIconArray[heroCounter].setImageResource(Hero.from(input)!!.getDrawable())

        heroCounter++
        if (heroCounter < 5) {
            heroIconArray[heroCounter].isClickable = true
            heroIconArray[heroCounter].setImageResource(R.drawable.ic_add_black_24dp)
        }
    }

    private var heroCounter = 0
    private var heroStringArray: ArrayList<String> = ArrayList()

    // Current Averages
    var avgEliminations: Long = 0
    var avgDeaths: Long = 0
    var avgDamage: Long = 0
    var avgHeals: Long = 0
    var avgAccuracy: Long = 0

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

    private lateinit var clearButton: Button
    private lateinit var submitButton: Button

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
        eliminations.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(string: Editable?) {}
            override fun beforeTextChanged(string: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(string: CharSequence?, start: Int, before: Int, count: Int) {
                if(!string.isNullOrBlank())
                    compareToAverage(eliminations, string?.toString()?.toLong() ?: 0, avgEliminations)
            }
        })
        deaths = view.findViewById(R.id.deaths)
        deaths.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(string: Editable?) {}
            override fun beforeTextChanged(string: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(string: CharSequence?, start: Int, before: Int, count: Int) {
                if(!string.isNullOrBlank())
                    compareToAverage(deaths, avgDeaths, string?.toString()?.toLong() ?: 1000) // Reversed because lower deaths is better
            }
        })
        damage = view.findViewById(R.id.damage)
        damage.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(string: Editable?) {}
            override fun beforeTextChanged(string: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(string: CharSequence?, start: Int, before: Int, count: Int) {
                if(!string.isNullOrBlank())
                    compareToAverage(damage, string?.toString()?.toLong() ?: 0, avgDamage)
            }
        })
        heals = view.findViewById(R.id.healing)
        heals.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(string: Editable?) {}
            override fun beforeTextChanged(string: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(string: CharSequence?, start: Int, before: Int, count: Int) {
                if(!string.isNullOrBlank())
                    compareToAverage(heals, string?.toString()?.toLong() ?: 0, avgHeals)
            }
        })
        accuracy = view.findViewById(R.id.accuracy)
        accuracy.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(string: Editable?) {}
            override fun beforeTextChanged(string: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(string: CharSequence?, start: Int, before: Int, count: Int) {
                if(!string.isNullOrBlank())
                    compareToAverage(accuracy, string?.toString()?.toLong() ?: 0, avgAccuracy)
            }
        })
        length = view.findViewById(R.id.length)
        sr = view.findViewById(R.id.sr)

        clearButton = view.findViewById(R.id.clearButton)
        clearButton.setOnClickListener(clearFields)

        submitButton = view.findViewById(R.id.submitButton)
        submitButton.setOnClickListener(submitMatch)

        parent = activity as MainActivity

        heroStringArray.clear()
        heroIconArray = arrayOf(hero1icon, hero2icon, hero3icon, hero4icon, hero5icon)
        for (icon in heroIconArray) {
            icon.setOnClickListener(addCharacter)
        }

        loadAverages()

        return view
    }

    // Determines field averages
    fun loadAverages() {
        if(parent.allGameArray.size == 0) return
        var runningEliminations: Long = 0
        var runningDeaths: Long = 0
        var runningDamage: Long = 0
        var runningHeals: Long = 0
        var runningAccuracy: Long = 0

        for (game in parent.allGameArray) {
            runningEliminations += (game.child("eliminations").value as Long)
            runningDeaths += (game.child("deaths").value as Long)
            runningDamage += (game.child("damage").value as Long)
            runningHeals += (game.child("heals").value as Long)
            runningAccuracy += (game.child("accuracy").value as Long)
        }

        avgEliminations = runningEliminations / parent.allGameArray.size
        avgDeaths = runningDeaths / parent.allGameArray.size
        avgDamage = runningDamage / parent.allGameArray.size
        avgHeals = runningHeals / parent.allGameArray.size
        avgAccuracy = runningAccuracy / parent.allGameArray.size
    }

    // Compares Text Fields to averages to show immediate good/bad
    private fun compareToAverage(view: EditText, input: Long, avg: Long) {
        when {
            input < avg -> view.setTextColor(ContextCompat.getColor(parent.baseContext, R.color.negative))
            input > avg -> view.setTextColor(ContextCompat.getColor(parent.baseContext, R.color.positive))
            else -> view.setTextColor(ContextCompat.getColor(parent.baseContext, R.color.colorDark))
        }
    }

    // Deletes character, cascades others down
    private var deleteCharacter = View.OnClickListener {
        deleteCharacter(it as ImageButton)
    }

    private fun deleteCharacter(view: ImageButton) {
        var marker = view.tag as Int
        heroStringArray.removeAt(marker)
        while (marker+1 < heroCounter) {
            heroIconArray[marker].setImageResource(Hero.from(heroStringArray[marker])!!.getDrawable())
            marker++
        }
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
            heroStringArray.size == 0
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

        // Grab previous info
        val oldSr: Int = parent.sr
        val newSr: Int = sr.text.toString().toInt()
        var matchCount: Int

        var win = 0 // WIN = 1, DRAW = 0, LOSS = -1
        if (oldSr < newSr) win = 1
        if (oldSr > newSr) win = -1
        var winRate= 0.0F
        var winCount: Int

        val time = Calendar.getInstance().timeInMillis
        val timeString = time.toString()

        // Submit Match
        val match = Match(
                time,
                winRate,
                newSr,
                win,
                mapSpinner.selectedItem.toString(),
                heroStringArray,
                eliminations.text.toString().toInt(),
                damage.text.toString().toInt(),
                heals.text.toString().toInt(),
                deaths.text.toString().toInt(),
                accuracy.text.toString().toInt(),
                length.text.toString().toInt())
        userPath.child("matches").child(timeString).setValue(match)

        // Post-Match updates
        userPath.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snap: DataSnapshot) {
                // Update Match Count
                if(snap.child("matchCount").value == null) {
                    matchCount = 1
                    winRate = if (win > 0) 1F else 0F
                    winCount = if (win > 0) 1 else 0
                    if (win > 0) userPath.child("careerHigh").setValue(newSr)
                    else userPath.child("careerHigh").setValue(oldSr)
                } else {
                    matchCount = (snap.child("matchCount").value as Long).toInt()
                    matchCount++
                    winCount = (snap.child("winCount").value as Long).toInt()
                    if (win > 0) winCount++
                    winRate = winCount.toFloat() / matchCount.toFloat()
                    if (newSr > snap.child("careerHigh").value as Long) userPath.child("careerHigh").setValue(newSr)
                }
                // Updates Profile Data
                userPath.child("matchCount").setValue(matchCount)
                userPath.child("winCount").setValue(winCount)
                userPath.child("winRate").setValue(winRate)
                userPath.child("sr").setValue(newSr)
                snap.child("careerHigh").value
                // Updates Match Win Rate
                userPath.child("matches").child(timeString).child("winRate").setValue(winRate)
                parent.latestSnap = snap
                parent.allGameArray.clear()
                parent.latestSnap!!.child("matches").children.forEach {
                    parent.allGameArray.add(it)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "whoops", Toast.LENGTH_SHORT).show()
            }
        })

        clearFields()
        Toast.makeText(context, "Submitted Match", Toast.LENGTH_SHORT).show()
    }
}