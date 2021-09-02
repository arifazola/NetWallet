package com.main.netwallet

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.main.netwallet.addTransaction.AddTransactionFragment
import com.main.netwallet.database.NetWalletDatabase
import com.main.netwallet.databinding.FragmentReminderBinding
import com.main.netwallet.initialSetting.AddNewAccountFragment
import com.main.netwallet.reminder.ReminderFragementViewModel
import com.main.netwallet.reminder.ReminderFragementViewModelFactory
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {
    val PREF_KEY_EMAIL = "email preference"
    lateinit var sharedPreferenceEmail : SharedPreferences
    private lateinit var drawerID : DrawerLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navController = this.findNavController(R.id.navHostFragment)

        //add bottomNavigation
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bn_main)
        val fab = findViewById<FloatingActionButton>(R.id.fab)

        fab.setOnClickListener {
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
            }else if(destination.id == R.id.transactionNotificationFragment) {
                bottomNavigationView.visibility = View.GONE
                fab.visibility = View.GONE
            }else{
                bottomNavigationView.visibility = View.VISIBLE
                fab.visibility = View.VISIBLE
            }
        }

        sharedPreferenceEmail = getSharedPreferences(PREF_KEY_EMAIL, Context.MODE_PRIVATE)
        val getEmail = sharedPreferenceEmail.getString("email_preference", null)
        val application = requireNotNull(this).application
        val dataSource = NetWalletDatabase.getInstance(application).netWalletDatabaseDao
        val todayDate: String =
            SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
        val viewModelProvider = ReminderFragementViewModelFactory(dataSource, application, getEmail.toString(), todayDate)
        val viewModel = ViewModelProvider(this, viewModelProvider).get(ReminderFragementViewModel::class.java)

        viewModel.getReminderDate.observe(this, Observer { list->
           list?.let {
               for (i in 0..list.size -1) {
                   if (i >= 0){
                   val getReminderDate = list.get(0).getReminderDate
                    val compare = getReminderDate.equals(todayDate)

                   if (getReminderDate == todayDate) {
                       val toNotifFragment = intent.getStringExtra("toNotifFragment")
                       val goToReminder = intent.getStringExtra("TransactionReminderFragment")
                       if (!toNotifFragment.equals("NotifFragment") && !goToReminder.equals("GoToReminderFragment")){
                           notif("You Have Pending Transaction", list.get(0).getReminderDetails)
                       }
                       Log.e("Notif", getReminderDate)
                       Log.e("Notif", todayDate)
                       Log.e("Notif", compare.toString())
                   }
               }
               }
           }
        })

        val toNotifFragment = intent.getStringExtra("toNotifFragment")
        if (toNotifFragment.equals("NotifFragment")){
            findNavController(R.id.navHostFragment).navigate(R.id.accountFragment)
        }

        val goToReminder = intent.getStringExtra("TransactionReminderFragment")
        if (goToReminder.equals("GoToReminderFragment")){
            findNavController(R.id.navHostFragment).navigate(R.id.transactionNotificationFragment)
        }

        Log.e("Activity", "Main Activity")
        Log.e("toReminder", intent.getStringExtra("toReminder").toString())


    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
        when(menuItem.itemId){
            R.id.logout -> {
                val intent = Intent(this, LogoutActivity::class.java)
                startActivity(intent)
                return@OnNavigationItemSelectedListener true
            }
            R.id.account ->{
                findNavController(R.id.navHostFragment).navigate(R.id.accountFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.homeBottom ->{
                findNavController(R.id.navHostFragment).navigate(R.id.homeFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.settingBottom ->{
                findNavController(R.id.navHostFragment).navigate(R.id.settingsFragment)
                return@OnNavigationItemSelectedListener true
            }
            else -> false
        }
    }

    fun notif(titleNotif: String, contentNotif: String){
        createNotificationChannel()

        val intent = Intent(this, MainActivity::class.java)
            .putExtra("toNotifFragment", "NotifFragment")
            .apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent : PendingIntent = PendingIntent.getActivity(this,0, intent, 0)

        var notifBuilder = NotificationCompat.Builder(this, "CHANNEL_ID")
            .setSmallIcon(R.drawable.ic_baseline_notifications_24)
            .setContentTitle(titleNotif)
            .setContentText(contentNotif)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        with(NotificationManagerCompat.from(this)){
            notify(1, notifBuilder)
        }
    }

    private fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("CHANNEL_ID",name,importance).apply {
                description = descriptionText
            }

            val notificationManager : NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}