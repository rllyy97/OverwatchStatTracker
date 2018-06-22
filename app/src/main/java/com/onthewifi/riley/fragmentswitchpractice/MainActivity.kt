package com.onthewifi.riley.fragmentswitchpractice

import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.design.widget.BottomNavigationView
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var name : String = ""
    var sr = 0
    var mainHero : String = ""

    private lateinit var sharedPreferences : SharedPreferences

    private lateinit var fragmentAdapter: FragmentAdapter
    private lateinit var viewPager: ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        updateLocals()

        setContentView(R.layout.activity_main)

        fragmentAdapter = FragmentAdapter(supportFragmentManager)
        viewPager = findViewById(R.id.fragmentContainer)
        setupViewPager(viewPager)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    // Updates local variables from Shared Preferences
    private fun updateLocals() {
        name = sharedPreferences.getString("name","NULL")
        sr = sharedPreferences.getInt("sr",0)
        mainHero = sharedPreferences.getString("main","Orisa")
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

}
