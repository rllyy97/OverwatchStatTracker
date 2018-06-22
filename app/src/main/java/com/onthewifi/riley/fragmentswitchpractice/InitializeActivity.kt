package com.onthewifi.riley.fragmentswitchpractice

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.*

class InitializeActivity : AppCompatActivity() {

//    constructor(parent: MainActivity) : this() {
//        var parentActivity = parent
//    }

    private lateinit var title : TextView
    private lateinit var fab : FloatingActionButton
    private lateinit var nameInput : EditText
    private lateinit var srInput : EditText
    private lateinit var heroInput : Spinner

    private lateinit var sharedPreferences : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_initialize)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)

        title = findViewById(R.id.welcome_message)
        fab = findViewById(R.id.fab)
        fab.setOnClickListener(onClickListener)
        nameInput = findViewById(R.id.name_input)
        srInput = findViewById(R.id.sr_input)
        heroInput = findViewById(R.id.hero_input)
        heroInput.adapter = ArrayAdapter<Hero>(this, android.R.layout.simple_dropdown_item_1line, Hero.values())

        if (sharedPreferences.contains("name")) {
            title.text = "Edit Profile Info"
            nameInput.setText(sharedPreferences.getString("name","NULL"))
            srInput.setText(sharedPreferences.getInt("sr",0).toString())
            heroInput.setSelection(Hero.valueOf(sharedPreferences.getString("main","NULL")).getID())
        }

    }

    private val onClickListener: View.OnClickListener = View.OnClickListener {
        if (nameInput.text.isEmpty() || srInput.text.isEmpty()) {
            Toast.makeText(this, "Please enter your information", Toast.LENGTH_SHORT).show()
        } else {
            sharedPreferences.edit()
                    .putString("name",nameInput.text.toString())
                    .putInt("sr",srInput.text.toString().toInt())
                    .putString("main",heroInput.selectedItem.toString())
                    .apply()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    //  Fixes back on settings button activation vs initialization
    override fun onBackPressed() {
        super.onBackPressed()
        if (!sharedPreferences.getString("name","").isEmpty()) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } else {
            super.onBackPressed()
        }

    }

}