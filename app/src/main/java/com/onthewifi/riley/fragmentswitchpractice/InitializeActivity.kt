package com.onthewifi.riley.fragmentswitchpractice

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.EditText
import android.widget.Toast

class InitializeActivity : AppCompatActivity() {

//    constructor(parent: MainActivity) : this() {
//        var parentActivity = parent
//    }

    private lateinit var fab : FloatingActionButton
    private lateinit var nameInput : EditText
    private lateinit var srInput : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_initialize)
        fab = findViewById(R.id.fab)
        fab.setOnClickListener(onClickListener)
        nameInput = findViewById(R.id.name_input)
        srInput = findViewById(R.id.sr_input)

    }

    private val onClickListener: View.OnClickListener = View.OnClickListener {
        if (nameInput.text.isEmpty() || srInput.text.isEmpty()) {
            Toast.makeText(this, "Please enter your information", Toast.LENGTH_SHORT).show()
        } else {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

}