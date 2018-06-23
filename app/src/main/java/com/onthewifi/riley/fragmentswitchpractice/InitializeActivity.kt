package com.onthewifi.riley.fragmentswitchpractice

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.*
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.GoogleApiClient
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.onComplete

class InitializeActivity : AppCompatActivity() {

    private lateinit var title : TextView
    private lateinit var signInButton : SignInButton
    private lateinit var nameInput : EditText
    private lateinit var srInput : EditText
    private lateinit var heroInput : Spinner

    private lateinit var sharedPreferences : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_initialize)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)

        title = findViewById(R.id.welcome_message)
        signInButton = findViewById(R.id.sign_in_button)
        signInButton.setOnClickListener(onClickListener)
        nameInput = findViewById(R.id.name_input)
        srInput = findViewById(R.id.sr_input)
        heroInput = findViewById(R.id.hero_input)
        heroInput.adapter = ArrayAdapter<Hero>(this, android.R.layout.simple_dropdown_item_1line, Hero.values())

        if (sharedPreferences.contains("name")) {
            title.text = getString(R.string.edit_profile_info)
            nameInput.setText(sharedPreferences.getString("name","NULL"))
            srInput.setText(sharedPreferences.getInt("sr",0).toString())
            heroInput.setSelection(Hero.valueOf(sharedPreferences.getString("main","NULL")).getID())
        }

    }

    private val onClickListener: View.OnClickListener = View.OnClickListener {
        if (nameInput.text.isEmpty() || srInput.text.isEmpty()) {
            Toast.makeText(this, "Please enter your information", Toast.LENGTH_SHORT).show()
        } else {
            closeActivity()
        }
    }

    //  Fixes back on settings button activation vs initialization
    override fun onBackPressed() {
        super.onBackPressed()
        if (!sharedPreferences.getString("name","").isEmpty()) {
            closeActivity()
        } else {
            super.onBackPressed()
        }

    }

    private fun closeActivity() {
        sharedPreferences.edit()
                .putString("name",nameInput.text.toString())
                .putInt("sr",srInput.text.toString().toInt())
                .putString("main",heroInput.selectedItem.toString())
                .apply()
        val i = Intent()
        i.setClass(this, SignInActivity::class.java)
        startActivity(i)
        finish()
    }

}