package com.onthewifi.riley.fragmentswitchpractice

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var name : String = ""
    var sr : Int = 0
    var careerHigh: Int = 0
    var winRate = 0F
    var matchCount = 0

    var allGameArray: ArrayList<DataSnapshot> = ArrayList()

    // Firebase variables
    var user : FirebaseUser? = null
    private lateinit var auth : FirebaseAuth
    private lateinit var database : FirebaseDatabase
    lateinit var databaseRef : DatabaseReference
    var latestSnap : DataSnapshot? = null

    private lateinit var fragmentAdapter: FragmentAdapter
    lateinit var viewPager: LockableViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        adapter.addFragment(FragmentEmptyFrame())
        viewPager.adapter = adapter
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

    fun switchContent(fragment: Fragment, view: Int, tag: String) {
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_up, R.anim.slide_in_down, R.anim.slide_out_down)
        transaction.replace(view, fragment, tag)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    fun refreshGameArray() {
        allGameArray.clear()
        latestSnap!!.child("matches").children.forEach {
            allGameArray.add(it)
        }
    }

}
