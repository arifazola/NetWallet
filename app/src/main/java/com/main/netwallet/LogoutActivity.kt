package com.main.netwallet

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.main.netwallet.databinding.FragmentHomeBinding

class LogoutActivity : AppCompatActivity() {
    private val PREFS_KEY = "login preference"
    private val PREFS_KEY_EMAIL = "email preference"
    lateinit var sharedPreferences: SharedPreferences
    lateinit var emailSharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferences = getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE)
        emailSharedPreferences = getSharedPreferences(PREFS_KEY_EMAIL, Context.MODE_PRIVATE)



        val editor : SharedPreferences.Editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()

        val editor2 : SharedPreferences.Editor = emailSharedPreferences.edit()
        editor2.clear()
        editor2.apply()

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)

    }
}