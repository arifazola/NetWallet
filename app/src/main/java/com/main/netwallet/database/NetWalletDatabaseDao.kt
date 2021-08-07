package com.main.netwallet.database

import androidx.room.Dao
import androidx.room.Query

@Dao
interface NetWalletDatabaseDao {

    @Query("Insert Into user_table (email,password,first_name,last_name) Values(:email,:password,:firstName,:lastName)")
    suspend fun insertUser(email: String, password: String, firstName: String, lastName: String)

    @Query("Select * From user_table where email=:email")
    suspend fun getEmail(email:String) : UserTable?

    @Query("Select * From user_table where email=:email And password=:password")
    suspend fun loginCheck(email: String, password: String) : UserTable?

    @Query("Insert Into user_table (currency) Values(:currency)")
    suspend fun insertCurrency(currency: String)

    @Query("Insert Into wallet_current_balance (wallet_type, balance, currency) Values(:walletType, :balance, :currency)")
    suspend fun insertWalletDetails(walletType: String, balance: Long, currency: String)

}