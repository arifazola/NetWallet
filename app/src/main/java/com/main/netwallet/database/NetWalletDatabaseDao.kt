package com.main.netwallet.database

import android.telecom.Call
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import java.util.*

@Dao
interface NetWalletDatabaseDao {

    @Query("Insert Into user_table (email,password,first_name,last_name) Values(:email,:password,:firstName,:lastName)")
    suspend fun insertUser(email: String, password: String, firstName: String, lastName: String)

    @Query("Select * From user_table where email=:email")
    suspend fun getEmail(email:String) : UserTable?

    @Query("Select * From user_table where email=:email And password=:password")
    suspend fun loginCheck(email: String, password: String) : UserTable?

//    @Query("Insert Into user_table (currency) Values(:currency)")
//    suspend fun insertCurrency(currency: String)

//    @Query("Update user_table Set currency=:currency Where email = :email")
//    suspend fun insertCurrency(currency: String, email: String)

    @Query("Update user_table Set currency=:currency, has_initialized=:hasInitialized Where email = :email")
    suspend fun insertCurrency(currency: String, hasInitialized: Boolean, email: String)

//    @Query("Insert Into wallet_current_balance (email, wallet_type, balance, currency) Values(:email, :walletType, :balance, :currency)")
//    suspend fun insertWalletDetails(email: String, walletType: String, balance: Long, currency: String)

//    @Query("Update user_table Set has_initialized=:hasInitialized")
//    suspend fun hasInitialized(hasInitialized: Boolean)

    @Query("Select * From user_table where email=:email")
    suspend fun getHasInitialized(email:String) : UserTable?

    //Query For First Intialization account
    @Query("Insert Into transaction_history (email, value, transaction_type, details, wallet_type, currency, date, bank_account_name) Values(:email,:value, :transactionType, :details ,:walletType, :currency, :date, :bankDetails)")
    suspend fun firstInitialize(email: String, value: Long, transactionType: String, details: String, walletType: String, currency: String, date: String, bankDetails: String)

    //Query for adding transaction
    @Query("Insert Into transaction_history (email, value, transaction_type, details, wallet_type, currency, date, bank_account_name) Values(:email, :value, :transactionType, :details, :walletType, :currency, :date, :bankDetails)")
    suspend fun addTransaction(email: String, value: Long, transactionType: String, details: String, walletType: String, currency: String, date: String, bankDetails: String)

    @Query("Select Sum(value) as value, transaction_type as total from transaction_history where email=:email AND wallet_type=:walletType group by transaction_type")
    fun sumTransaction(email: String, walletType: String) : LiveData<List<SumTransaction>>

    //Query to show all transaction
    @Query("Select * from transaction_history where wallet_type=:walletType")
    fun getTransaction(walletType: String) : LiveData<List<TransactionHistory>>

    //Query to show all account type
    @Query("Select wallet_type from transaction_history where details='Account Opening' AND email=:email")
    fun getAccountList(email: String): LiveData<List<AccountList>>

    //Query to switch account
    @Query("Select wallet_type, currency, bank_account_name from transaction_history where email=:email And wallet_type=:walletType Limit 1")
    fun switchAccount(email:String, walletType: String): LiveData<List<SwitchAccount>>

    //Query to input reminder
    @Query("Insert Into reminder_table (email, reminder_date, reminder_details) VALUES(:email, :reminderDate, :reminderDetails)")
    suspend fun addReminder(email: String, reminderDate: String, reminderDetails: String)

    //Query to get reminder date
    @Query("Select reminder_date from reminder_table WHERE email=:email")
    fun getReminderDate(email: String) : LiveData<List<GetReminderDate>>
}