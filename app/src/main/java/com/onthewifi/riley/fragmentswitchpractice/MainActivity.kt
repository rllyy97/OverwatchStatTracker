package com.onthewifi.riley.fragmentswitchpractice

import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.design.widget.BottomNavigationView
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var name : String = ""
    var sr = 0
    var mainHero : String = ""
    // Firebase variables
    var user : FirebaseUser? = null
    lateinit var auth : FirebaseAuth
    lateinit var database : FirebaseDatabase
    lateinit var databaseRef : DatabaseReference

    private lateinit var sharedPreferences : SharedPreferences

    private lateinit var fragmentAdapter: FragmentAdapter
    private lateinit var viewPager: ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Grab info from Initializer
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        name = sharedPreferences.getString("name","NULL")
        auth = FirebaseAuth.getInstance()
        val bundle = this.intent.extras
        if(bundle != null) {
            user = firebaseAuthWithGoogle(bundle.getParcelable("user"))
//            Toast.makeText(this, bundle.getParcelable<GoogleSignInAccount>("user").displayName, Toast.LENGTH_SHORT).show()
        }
        database = FirebaseDatabase.getInstance()
        databaseRef = FirebaseDatabase.getInstance().reference

        setContentView(R.layout.activity_main)
        fragmentAdapter = FragmentAdapter(supportFragmentManager)
        viewPager = findViewById(R.id.fragmentContainer)
        setupViewPager(viewPager)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    // Initializes new fragments and adapter
    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = FragmentAdapter(supportFragmentManager)
        // First fragment added is shown
        adapter.addFragment(FragmentProfile())
        adapter.addFragment(FragmentNewMatch())
        adapter.addFragment(FragmentTracker())
        viewPager.adapter = adapter
        // Disables swiping
        viewPager.beginFakeDrag()
    }

    //  Function to control bottom navigation clicks
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                viewPager.currentItem = 0
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                viewPager.currentItem = 1
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                viewPager.currentItem = 2
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    // Function to set ViewPager fragment from other activities
//    fun setViewPager(fragmentInt: Int) {
//        viewPager.currentItem = fragmentInt
//    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount): FirebaseUser? {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential)
        return auth.currentUser
    }

}
