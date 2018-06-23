package com.onthewifi.riley.fragmentswitchpractice

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {

    private lateinit var sharedPreferences : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        if (!sharedPreferences.contains("name")) {
            startActivity(Intent(this, InitializeActivity::class.java))
        } else {
            startActivity(Intent(this, SignInActivity::class.java))
        }

        startActivity(Intent(this, InitializeActivity::class.java))
        finish()
    }
}