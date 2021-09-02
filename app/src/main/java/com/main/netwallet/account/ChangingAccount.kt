package com.main.netwallet.account

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.main.netwallet.MainActivity
import com.main.netwallet.R
import com.main.netwallet.database.NetWalletDatabase

class ChangingAccount : AppCompatActivity() {
    private val PREFS_KEY = "account wallet preference"
    val PREF_KEY_EMAIL = "email preference"
    lateinit var sharedPreferencesAccountWallet: SharedPreferences
    lateinit var emailSharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sharedPreferencesAccountWallet = this.getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE)
        emailSharedPreferences = this.getSharedPreferences(PREF_KEY_EMAIL, Context.MODE_PRIVATE)
        val getEmail = emailSharedPreferences.getString("email_preference", null)
        val walletTypePreference = sharedPreferencesAccountWallet.getString("wallet_type", null)
        val currencyPreference = sharedPreferencesAccountWallet.getString("currency", null)
        val bankAccountNamePreference =
            sharedPreferencesAccountWallet.getString("bank_account_name", null)
//        val getWalletType = sharedPreferencesAccountWallet.getString("wallet_type", null)
//        destoryWallet()
        val application = requireNotNull(this).application
        val dataSource = NetWalletDatabase.getInstance(application).netWalletDatabaseDao
//        val email = "afazola@gmail.com"
        val viewModelProvider = AccountViewModelProvider(
            dataSource,
            application,
            getEmail.toString(),
            walletTypePreference.toString()
        )
        val viewModel = ViewModelProvider(this, viewModelProvider).get(AccountViewModel::class.java)

        viewModel.switchAccount.observe(this, Observer { list ->
            list?.let {
                val walletType = list.get(0).walletType.toString()
                val currency = list.get(0).currency.toString()
                val bankAccountName = list.get(0).bankAccountName.toString()

                switchAccount(walletTypePreference.toString(),currency, bankAccountName)
                Log.e("Change", walletTypePreference.toString() + currency + bankAccountName)
            }
        })


        val toReminder = intent.getStringExtra("toReminder")
        if(toReminder.equals("ReminderFragment")){
            val intent = Intent(this, MainActivity::class.java).putExtra("TransactionReminderFragment","GoToReminderFragment")
            startActivity(intent)
            this.finish()
        }else {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            this.finish()
        }

        Log.e("Activity", "Changing Account")
    }

    private fun switchAccount(walletType: String, currency: String, bankAccountName: String){
        val editor : SharedPreferences.Editor = sharedPreferencesAccountWallet.edit()
        editor.putString("wallet_type", walletType)
        editor.putString("currency", currency)
        editor.putString("bank_account_name", bankAccountName)
        editor.apply()
    }
}