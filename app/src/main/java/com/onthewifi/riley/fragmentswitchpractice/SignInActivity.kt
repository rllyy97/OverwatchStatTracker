package com.onthewifi.riley.fragmentswitchpractice

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import org.jetbrains.anko.progressDialog
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.support.constraint.ConstraintLayout
import android.support.design.widget.Snackbar
import com.firebase.ui.auth.ui.phone.CountryListSpinner


class SignInActivity : AppCompatActivity(), GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    private val TAG = "8"
    private val RC_SIGN_IN = 925
    private var bundle = Bundle()
    // Google Authentication objects
    private lateinit var gso : GoogleSignInOptions
    private lateinit var gac : GoogleApiClient

    private lateinit var baseView : ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        baseView = findViewById(R.id.base)
        if(!isNetworkConnected()) {
            Snackbar.make(baseView, R.string.no_internet_warning,
                    Snackbar.LENGTH_SHORT)
                    .show()
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
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun closeActivity() {
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