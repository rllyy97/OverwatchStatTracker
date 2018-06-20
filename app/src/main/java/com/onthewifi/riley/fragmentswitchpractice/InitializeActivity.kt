package com.onthewifi.riley.fragmentswitchpractice

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.view.View

class InitializeActivity() : AppCompatActivity() {

    constructor(parent: MainActivity) : this() {
        var parentActivity = parent
    }

    lateinit var fab : FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_initialize)
        fab = findViewById(R.id.fab)
        fab.setOnClickListener(onClickListener)

    }

    private val onClickListener: View.OnClickListener = View.OnClickListener {
        finish()
    }

}