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
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.GoogleApiClient
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.onComplete

class InitializeActivity : AppCompatActivity(), GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

//    constructor(parent: MainActivity) : this() {
//        var parentActivity = parent
//    }

    private val TAG = "8"
    private val RC_SIGN_IN = 925

    private lateinit var title : TextView
//    private lateinit var fab : FloatingActionButton
    private lateinit var signInButton : SignInButton
    private lateinit var nameInput : EditText
    private lateinit var srInput : EditText
    private lateinit var heroInput : Spinner

    // Google Authentication objects
    private lateinit var gso : GoogleSignInOptions
    private lateinit var gac : GoogleApiClient

    private lateinit var sharedPreferences : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_initialize)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)

        title = findViewById(R.id.welcome_message)
//        fab = findViewById(R.id.fab)
//        fab.setOnClickListener(onClickListener)
        signInButton = findViewById(R.id.sign_in_button)
        signInButton.setOnClickListener(onClickListener)
        nameInput = findViewById(R.id.name_input)
        srInput = findViewById(R.id.sr_input)
        heroInput = findViewById(R.id.hero_input)
        heroInput.adapter = ArrayAdapter<Hero>(this, android.R.layout.simple_dropdown_item_1line, Hero.values())

        // Initialize Firebase Objects
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        gac = GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build()

        if (sharedPreferences.contains("name")) {
            title.text = R.string.edit_profile_info.toString()
            nameInput.setText(sharedPreferences.getString("name","NULL"))
            srInput.setText(sharedPreferences.getInt("sr",0).toString())
            heroInput.setSelection(Hero.valueOf(sharedPreferences.getString("main","NULL")).getID())
        }

    }

    private val onClickListener: View.OnClickListener = View.OnClickListener {
        if (nameInput.text.isEmpty() || srInput.text.isEmpty()) {
            Toast.makeText(this, "Please enter your information", Toast.LENGTH_SHORT).show()
        } else {
            doAsync {
                signIn()
                onComplete {
                    closeActivity()
                }
            }
        }
    }

    // Firebase Google Authentication
    private fun signIn() {
        val signInIntent = Auth.GoogleSignInApi.getSignInIntent(gac)
        startActivityForResult(signInIntent, RC_SIGN_IN)
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

    private fun closeActivity() {
        sharedPreferences.edit()
                .putString("name",nameInput.text.toString())
                .putInt("sr",srInput.text.toString().toInt())
                .putString("main",heroInput.selectedItem.toString())
                .apply()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            var result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            handleSignInResult(result)
        }
    }

    private fun handleSignInResult(result: GoogleSignInResult) {
        Log.d(TAG, "Sign in result: " + result.isSuccess)
        if(result.isSuccess) {
            var account = result.signInAccount
            // Put the account somewhere
        }
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onClick(v: View?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}