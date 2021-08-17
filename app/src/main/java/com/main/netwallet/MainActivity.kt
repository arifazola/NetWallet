package com.main.netwallet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.main.netwallet.addTransaction.AddTransactionFragment


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        val navController = this.findNavController(R.id.navHostFragment)

        //add bottomNavigation
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bn_main)
        val fab = findViewById<FloatingActionButton>(R.id.fab)

        fab.setOnClickListener {
//            val destination = AddTransactionFragment()
//            supportFragmentManager.beginTransaction().replace(R.id.navHostFragment, destination).commit()
            findNavController(R.id.navHostFragment).navigate(R.id.addTransactionFragment)
        }

        bottomNavigationView.setOnItemSelectedListener(mOnNavigationItemSelectedListener)

        /**
         * These lines of codes below to check destination changed
         */
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if(destination.id == R.id.loginFragment){
                bottomNavigationView.visibility = View.GONE
                fab.visibility = View.GONE
            }else if(destination.id == R.id.registerFragment){
                bottomNavigationView.visibility = View.GONE
                fab.visibility = View.GONE
            }else if(destination.id == R.id.initialSettingFragment) {
                bottomNavigationView.visibility = View.GONE
                fab.visibility = View.GONE
            }else if(destination.id == R.id.addTransactionFragment){
                fab.visibility = View.GONE
            }else{
                bottomNavigationView.visibility = View.VISIBLE
                fab.visibility = View.VISIBLE
            }
        }

    }
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
        when(menuItem.itemId){
            R.id.logout -> {
//                val destination = LogoutActivity()
//                supportFragmentManager.beginTransaction().replace(R.id.containter,destination,destination.javaClass.simpleName)
//                    .commit()
//                return@OnNavigationItemSelectedListener true
                val intent = Intent(this, LogoutActivity::class.java)
                startActivity(intent)
                return@OnNavigationItemSelectedListener true
            }
            else -> false
        }
    }
}