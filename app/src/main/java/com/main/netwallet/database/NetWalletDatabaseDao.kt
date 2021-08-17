package com.main.netwallet.database

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
    @Query("Insert Into transaction_history (email, value, transaction_type, details, wallet_type, currency, date) Values(:email,:value, :transactionType, :details ,:walletType, :currency, :date)")
    suspend fun firstInitialize(email: String, value: Long, transactionType: String, details: String, walletType: String, currency: String, date: String)

    //Query for adding transaction
    @Query("Insert Into transaction_history (email, value, transaction_type, details, wallet_type, currency, date) Values(:email, :value, :transactionType, :details, :walletType, :currency, :date)")
    suspend fun addTransaction(email: String, value: Long, transactionType: String, details: String, walletType: String, currency: String, date: String)

    @Query("Select Sum(value) as value, transaction_type as total from transaction_history group by transaction_type")
    fun sumTransaction() : LiveData<List<SumTransaction>>

    //Query to show all transaction
    @Query("Select * from transaction_history")
    fun getTransaction() : LiveData<List<TransactionHistory>>



}