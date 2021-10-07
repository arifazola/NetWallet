package com.main.netwallet.notif

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.main.netwallet.MainActivity
import com.main.netwallet.database.GetReminderDate
import com.main.netwallet.database.NetWalletDatabase
import com.main.netwallet.database.NetWalletDatabaseDao
import com.main.netwallet.makeStatusNotification
import com.main.netwallet.reminder.ReminderFragementViewModel
import com.main.netwallet.reminder.ReminderFragementViewModelFactory
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.util.*

class AlarmBroadcastReceiver: BroadcastReceiver() {
    val PREF_KEY_EMAIL = "email preference"
    lateinit var sharedPreferenceEmail: SharedPreferences
    private val PREFS_KEY_NOTIF= "notif details preference"
    private lateinit var sharedPreferenceNotifDetails: SharedPreferences
    override fun onReceive(context: Context?, intent: Intent?) {
        if(context != null && intent != null){
                makeStatusNotification(
                    "Hey, You Have Set A Reminder. Click To Check",
                    context,
                    "Pending Transaction"
                )
        }
    }

//    fun getNotifDetails(databaseDao: NetWalletDatabaseDao, context: Context) : String{
//        sharedPreferenceEmail = context.getSharedPreferences(PREF_KEY_EMAIL, Context.MODE_PRIVATE)
//        val getEmail = sharedPreferenceEmail.getString("email_preference", null)
//        val result = databaseDao.getReminderDate(getEmail.toString())
//        return result.value!!.get(0).getReminderDetails
//    }


}