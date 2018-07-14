package com.onthewifi.riley.fragmentswitchpractice

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient


class SignInActivity : AppCompatActivity(), GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    private val rcSignIn = 456
    private var bundle = Bundle()
    // Google Authentication objects
    private lateinit var gso : GoogleSignInOptions
    private lateinit var gac : GoogleApiClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        if(!isNetworkConnected()) {
            Toast.makeText(baseContext, R.string.no_internet_warning, Toast.LENGTH_LONG).show()
        }
        // Initialize Firebase Objects
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        gac = GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build()

        signIn()
    }

    // Firebase Google Authentication
    private fun signIn() {
        val signInIntent = Auth.GoogleSignInApi.getSignInIntent(gac)
        startActivityForResult(signInIntent, rcSignIn)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == rcSignIn) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            if(result.isSuccess) {
                val user = result.signInAccount
                bundle.putParcelable("user",user)
                //  Initialization Complete
                closeActivity()
            }
        }

    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onClick(v: View?) {
        return
    }

    private fun closeActivity() {
        val i = Intent()
        i.putExtras(bundle)
        i.setClass(this, MainActivity::class.java)
        startActivity(i)
        finish()
    }

    private fun isNetworkConnected(): Boolean {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        return cm.activeNetworkInfo != null
    }
}