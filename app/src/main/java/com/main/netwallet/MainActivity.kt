package com.main.netwallet

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
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
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.main.netwallet.addTransaction.AddTransactionFragment
import com.main.netwallet.database.NetWalletDatabase
import com.main.netwallet.databinding.FragmentReminderBinding
import com.main.netwallet.initialSetting.AddNewAccountFragment
import com.main.netwallet.monthlyTransaction.SetMonthlyFragmentViewModel
import com.main.netwallet.monthlyTransaction.SetMonthlyFragmentViewModelProvider
import com.main.netwallet.notif.AlarmBroadcastReceiver
import com.main.netwallet.reminder.ReminderFragementViewModel
import com.main.netwallet.reminder.ReminderFragementViewModelFactory
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashSet


class MainActivity : AppCompatActivity() {
    private val PREF_KEY_EMAIL = "email preference"
    private lateinit var sharedPreferenceEmail: SharedPreferences
    private val PREFS_KEY_NOTIF= "notif details preference"
    private lateinit var sharedPreferenceNotifDetails: SharedPreferences
    private lateinit var drawerID: DrawerLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navController = this.findNavController(R.id.navHostFragment)
        val toNotifFragment = intent.getStringExtra("toNotifFragment")
        sharedPreferenceEmail = getSharedPreferences(PREF_KEY_EMAIL, Context.MODE_PRIVATE)

        //add bottomNavigation
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bn_main)
//        val fab = findViewById<FloatingActionButton>(R.id.fab)
//
//        fab.setOnClickListener {
//            findNavController(R.id.navHostFragment).navigate(R.id.addTransactionFragment)
//        }

        bottomNavigationView.setOnItemSelectedListener(mOnNavigationItemSelectedListener)

        /**
         * These lines of codes below to check destination changed
         */
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.loginFragment) {
                bottomNavigationView.visibility = View.GONE
//                fab.visibility = View.GONE
            } else if (destination.id == R.id.registerFragment) {
                bottomNavigationView.visibility = View.GONE
//                fab.visibility = View.GONE
            } else if (destination.id == R.id.initialSettingFragment) {
                bottomNavigationView.visibility = View.GONE
//                fab.visibility = View.GONE
            } else if (destination.id == R.id.addTransactionFragment) {
//                fab.visibility = View.GONE
            } else if (destination.id == R.id.transactionNotificationFragment) {
                bottomNavigationView.visibility = View.GONE
//            }else if(toNotifFragment.equals("NotifFragment")){
//                bottomNavigationView.visibility = View.GONE
//                fab.visibility = View.GONE
            } else {
                bottomNavigationView.visibility = View.VISIBLE
//                fab.visibility = View.VISIBLE
            }
        }


//        val getEmail = sharedPreferenceEmail.getString("email_preference", null)
//        val application = requireNotNull(this).application
//        val dataSource = NetWalletDatabase.getInstance(application).netWalletDatabaseDao
//        val todayDate: String =
//            SimpleDateFormat("dd MM yyyy", Locale.getDefault()).format(Date())
//        val dateFormat = SimpleDateFormat("dd MM yyyy")
//        val mDate : Date = dateFormat.parse(todayDate)
//        val dateInMili = mDate.time
//        val viewModelProvider = ReminderFragementViewModelFactory(dataSource, application, getEmail.toString(), 1631773500000)
//        val viewModel = ViewModelProvider(this, viewModelProvider).get(ReminderFragementViewModel::class.java)
//
//        Log.e("Email Date", "${getEmail.toString()} ${dateInMili.toString()}")
//
//        viewModel.getReminderDate.observe(this, Observer { list->
//           list?.let {
//               for (i in 0..list.size -1) {
//                   if (i >= 0){
//                   val getReminderDate = list.get(0).getReminderDate
//                    val compare = getReminderDate.equals(todayDate)
//
//                     //convert dateToday and date from database from Long to date
//                     val todayDateConvert = SimpleDateFormat("dd/MM/yyyy").format(Date(dateInMili))
//                     val dateDatabaseConvert = SimpleDateFormat("dd/MM/yyyy").format(getReminderDate)
//
//                   if (dateDatabaseConvert == todayDateConvert) {
//                       val toNotifFragment = intent.getStringExtra("toNotifFragment")
//                       val goToReminder = intent.getStringExtra("TransactionReminderFragment")
//                       if (!toNotifFragment.equals("NotifFragment") && !goToReminder.equals("GoToReminderFragment")){
//                           notif("You Have Pending Transaction", list.get(0).getReminderDetails)
//                       }
//                   }
//                       Log.e("Notif", getReminderDate.toString())
//                       Log.e("Notif", todayDate)
//                       Log.e("Notif", compare.toString())
//               }
//               }
//           }
//        })

        if (toNotifFragment.equals("NotifFragment")) {
            findNavController(R.id.navHostFragment).navigate(R.id.accountFragment)
        }

        val goToReminder = intent.getStringExtra("TransactionReminderFragment")
        if (goToReminder.equals("GoToReminderFragment")) {
            findNavController(R.id.navHostFragment).navigate(R.id.transactionNotificationFragment)
        }

        if (!toNotifFragment.equals("NotifFragment") && !goToReminder.equals("GoToReminderFragment")) {
            setAlarm()
        }

        val fromTransactionNotif = intent.getStringExtra("FromTransactionNotif")
        if(fromTransactionNotif.equals("is_true")){
            intent.replaceExtras(Bundle())
        }


        Log.e("Activity", "Main Activity")
        Log.e("toReminder", intent.getStringExtra("toReminder").toString())

        val getActivity : ActivityInfo = packageManager.getActivityInfo(this.componentName, 0)

        Log.e("Current Activity", getActivity.toString())

        setMonthly()

//        sharedPreferenceNotifDetails = getSharedPreferences(PREFS_KEY_NOTIF, Context.MODE_PRIVATE)
//
//        storeNotifDetails()
//
//        val gson = Gson()
//
//        val getNotifDetails: String? = sharedPreferenceNotifDetails.getString("notif_details", null)
//
//        val type: Type = object : TypeToken<java.util.ArrayList<String?>?>() {}.getType()
//
//        val result: String = gson.fromJson(getNotifDetails,type)

//        for (i in 0.. getNotifDetails!!.size -1){
//            Log.i("NotifDetails", result)
//        }

        saveArrayList("notif_details")

        for(i in 0.. getArrayList("notif_details")!!.size -1){
            Log.i("NotifDetails", getArrayList("notif_details")!!.get(i).toString())
        }

//        Log.i("NotifDetails", getArrayList("notif_details")!!.get(0).toString())

        getPreviousFragment()

    }

    private val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.logout -> {
                    val intent = Intent(this, LogoutActivity::class.java)
                    startActivity(intent)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.account -> {
                    findNavController(R.id.navHostFragment).navigate(R.id.accountFragment)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.homeBottom -> {
                    findNavController(R.id.navHostFragment).navigate(R.id.homeFragment)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.settingBottom -> {
                    findNavController(R.id.navHostFragment).navigate(R.id.settingsFragment)
                    return@OnNavigationItemSelectedListener true
                }
                else -> false
            }
        }

    fun notif(titleNotif: String, contentNotif: String) {
        createNotificationChannel()

        val intent = Intent(this, MainActivity::class.java)
            .putExtra("toNotifFragment", "NotifFragment")
//            .apply {
//                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//            }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        var notifBuilder = NotificationCompat.Builder(this, "CHANNEL_ID")
            .setSmallIcon(R.drawable.ic_baseline_notifications_24)
            .setContentTitle(titleNotif)
            .setContentText(contentNotif)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        with(NotificationManagerCompat.from(this)) {
            notify(1, notifBuilder)
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("CHANNEL_ID", name, importance).apply {
                description = descriptionText
            }

            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    /**
     * Register an alarm for notification
     * package com.main.netwallet.notif
     */
    private fun setAlarm() {
        sharedPreferenceEmail = getSharedPreferences(PREF_KEY_EMAIL, Context.MODE_PRIVATE)
        val getEmail = sharedPreferenceEmail.getString("email_preference", null)
        val application = requireNotNull(this).application
        val dataSource = NetWalletDatabase.getInstance(application).netWalletDatabaseDao
        val todayDate: String =
            SimpleDateFormat("dd MM yyyy 13:25:00", Locale.getDefault()).format(Date())
        val dateFormat = SimpleDateFormat("dd MM yyyy HH:mm:ss")
        val mDate: Date = dateFormat.parse(todayDate)
        val dateInMili = mDate.time
        val viewModelProvider = ReminderFragementViewModelFactory(
            dataSource,
            application,
            getEmail.toString(),
            dateInMili
        )
        val viewModel =
            ViewModelProvider(this, viewModelProvider).get(ReminderFragementViewModel::class.java)

        viewModel.getReminderDate.observe(this, Observer { list ->
            list?.let {
                val dataSize = list.size
                Log.e("size", dataSize.toString())

                for(i in 0.. list.size-1) {
                    val alarmManager =
                        applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                    val intent = Intent(this, AlarmBroadcastReceiver::class.java)
                    val pendingIntent = PendingIntent.getBroadcast(this, i, intent, 0)

                    alarmManager.setExact(
                        AlarmManager.RTC_WAKEUP,
                        list.get(i).getReminderDate,
                        pendingIntent
                    )
                    Log.e("Clock", list.get(i).getReminderDate.toString())

                    if(dateInMili > list.get(i).getReminderDate){
                        val alarmManager =
                            applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                        val intent = Intent(this, AlarmBroadcastReceiver::class.java)
                        val pendingIntent = PendingIntent.getBroadcast(this, i, intent, 0)

                        alarmManager.cancel(pendingIntent)
                    }
                }
            }
        })
    }

    private fun setMonthly(){
        val getEmail = sharedPreferenceEmail.getString("email_preference", null)
        val application = requireNotNull(this).application
        val dataSource = NetWalletDatabase.getInstance(application).netWalletDatabaseDao
        val viewModelProvider = SetMonthlyFragmentViewModelProvider(dataSource,application,getEmail.toString())
        val viewModel = ViewModelProvider(this,viewModelProvider).get(SetMonthlyFragmentViewModel::class.java)
        val alarmManager = applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        viewModel.scheduledAt.observe(this, Observer { list->
            list?.let {
                if (list.size >=1){
                    for (i in 0.. list.size-1){
                        val calendar = Calendar.getInstance()
                        val date = list.get(i).scheduledAt.toString()
                        val month = SimpleDateFormat("MM").format(calendar.time)
                        val year = calendar.get(Calendar.YEAR)
                        val hour = calendar.get(Calendar.HOUR_OF_DAY)
                        val minute = calendar.get(Calendar.MINUTE)
                        val second = Random().nextInt(60).toString()
                        val scheduled = "$date $month $year $hour:$minute:$second"
                        val dateFormat = SimpleDateFormat("dd MM yyyy HH:mm:ss")
                        val toMil = dateFormat.parse(scheduled)
                        val resToMil = toMil.time
                        val milToDate = SimpleDateFormat("dd MM yyyy").format(resToMil)
                        val today = SimpleDateFormat("dd MM yyyy").format(Date())
                        val todayDateFormat = SimpleDateFormat("dd MM yyyy")
                        val todayParse = todayDateFormat.parse(today)
                        val todayToMil = todayParse.time

                        if(today.equals(milToDate)){
                            viewModel.addTransaction(getEmail.toString(), list.get(i).value, list.get(i).transactionType, list.get(i).details, list.get(i).walletType, list.get(i).currency, todayToMil, list.get(i).bankAccountName!!)
                            viewModel.delete(list.get(i).id)
//                            Log.i("Res","Equal")
//                            Log.i("Res", today.toString())
//                            Log.i("Res", milToDate.toString())
                        }else{
                            Log.i("Res","Not equal")
                            Log.i("Res", today.toString())
                            Log.i("Res", milToDate.toString())
                        }

                        Log.i("Date in mill $i", resToMil.toString())
                        Log.i("Mill to date $i", milToDate.toString())

                    }
                }
            }
        })
    }

//    fun storeNotifDetails(){

//        val editor: SharedPreferences.Editor = sharedPreferenceNotifDetails.edit()
//        val gson = Gson()
//        val list: ArrayList<String> = arrayListOf<String>("Hahaha", "Hehehe")
//        val json = gson.toJson(list)
//        editor.putString("notif_details", json)
//        editor.apply()
//        sharedPreferenceEmail = getSharedPreferences(PREF_KEY_EMAIL, Context.MODE_PRIVATE)
//        val getEmail = sharedPreferenceEmail.getString("email_preference", null)
//        sharedPreferenceNotifDetails = getSharedPreferences(PREFS_KEY_NOTIF, Context.MODE_PRIVATE)
//        val application = requireNotNull(this).application
//        val dataSource = NetWalletDatabase.getInstance(application).netWalletDatabaseDao
//        val todayDate: String =
//            SimpleDateFormat("dd MM yyyy 13:25:00", Locale.getDefault()).format(Date())
//        val dateFormat = SimpleDateFormat("dd MM yyyy HH:mm:ss")
//        val mDate: Date = dateFormat.parse(todayDate)
//        val dateInMili = mDate.time
//        val viewModelProvider = ReminderFragementViewModelFactory(
//            dataSource,
//            application,
//            getEmail.toString(),
//            dateInMili
//        )
//        val viewModel =
//            ViewModelProvider(this, viewModelProvider).get(ReminderFragementViewModel::class.java)

//        var listValue: String? = null
//        val listDetails = mutableSetOf<String>("Hahaha", "Hehehe")

//        listValue = list.get(i).getReminderDetails
//        listDetails.add("Hahaha")
//        listDetails.add("Hehehe")



//        viewModel.getReminderDate.observe(this, Observer { list->
//            list?.let {
//                var listValue: String? = null
//                val listDetails = mutableSetOf<String>()
//                for(i in 0.. list.size-1){
//                    listValue = list.get(i).getReminderDetails
//                    listDetails.add(listValue)
//                }
//                val listDetails = HashSet<String>()
//                listDetails.add("Hahaha")
//                val editor : SharedPreferences.Editor = sharedPreferenceNotifDetails.edit()
//                editor.putStringSet("notif_details", listDetails)
//                editor.apply()
//            }
//        })
//    }

    fun saveArrayList(key: String?) {
        sharedPreferenceEmail = getSharedPreferences(PREF_KEY_EMAIL, Context.MODE_PRIVATE)
        val getEmail = sharedPreferenceEmail.getString("email_preference", null)
        sharedPreferenceNotifDetails = getSharedPreferences(PREFS_KEY_NOTIF, Context.MODE_PRIVATE)
        val application = requireNotNull(this).application
        val dataSource = NetWalletDatabase.getInstance(application).netWalletDatabaseDao
        val todayDate: String =
            SimpleDateFormat("dd MM yyyy 13:25:00", Locale.getDefault()).format(Date())
        val dateFormat = SimpleDateFormat("dd MM yyyy HH:mm:ss")
        val mDate: Date = dateFormat.parse(todayDate)
        val dateInMili = mDate.time
        val viewModelProvider = ReminderFragementViewModelFactory(
            dataSource,
            application,
            getEmail.toString(),
            dateInMili
        )
        val viewModel =
            ViewModelProvider(this, viewModelProvider).get(ReminderFragementViewModel::class.java)
        viewModel.getReminderDate.observe(this, Observer { list->
            list?.let {
                var listValue: String? = null
                val listDetails = arrayListOf<String>()
                for(i in 0.. list.size-1){
                    listValue = list.get(i).getReminderDetails
                    listDetails.add(listValue)
                }
                sharedPreferenceNotifDetails = getSharedPreferences(PREFS_KEY_NOTIF, Context.MODE_PRIVATE)
                val editor: SharedPreferences.Editor = sharedPreferenceNotifDetails.edit()
                val gson = Gson()
                val json: String = gson.toJson(listDetails)
                editor.putString(key, json)
                editor.apply()
            }
        })
    }

    fun getArrayList(key: String?): java.util.ArrayList<String?>? {
        sharedPreferenceNotifDetails = getSharedPreferences(PREFS_KEY_NOTIF, Context.MODE_PRIVATE)
        val gson = Gson()
        val json: String? = sharedPreferenceNotifDetails.getString(key, null)
        val type: Type = object : TypeToken<java.util.ArrayList<String?>?>() {}.getType()
        return gson.fromJson(json, type)
    }

    fun getPreviousFragment(){
        val fragmentManager = fragmentManager
        val count = fragmentManager.backStackEntryCount
        if(count >=1) {
            val result = fragmentManager.getBackStackEntryAt(count - 1).name
            Log.i("Previous Fragment", result)
        }
        Log.i("Previous Fragment", count.toString())
    }
}
