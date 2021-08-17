package com.main.netwallet.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*


@Entity(tableName = "user_table")
    data class UserTable(
        @PrimaryKey(autoGenerate = true)
        var userID : Int = 0,

        @ColumnInfo(name = "email")
        val email : String?,

        @ColumnInfo(name = "password")
        val password: String?,

        @ColumnInfo(name = "first_name")
        val firstName: String?,

        @ColumnInfo(name = "last_name")
        val lastName: String?,

        @ColumnInfo(name = "currency")
        val currency: String?,

        @ColumnInfo(name = "has_initialized")
        val hasInitialized: Boolean?

    )

//    @Entity(tableName = "wallet_current_balance")
//    data class WalletCurrentBalance(
//        @PrimaryKey(autoGenerate = true)
//        var ID : Int = 0,
//
//        @ColumnInfo(name = "email")
//        val email: String?,
//
//        @ColumnInfo(name = "wallet_type")
//        val walletType: String?,
//
//        @ColumnInfo(name = "balance")
//        val balance: Long?,
//
//        @ColumnInfo(name = "currency")
//        val currency: String?
//    )

    @Entity(tableName = "transaction_history")
    data class TransactionHistory(
        @PrimaryKey(autoGenerate = true)
        var id : Int = 0,

        @ColumnInfo(name = "email")
        val email: String?,

        @ColumnInfo(name = "value")
        val value: String?,

        @ColumnInfo(name = "transaction_type")
        val transactionType: String?,

        @ColumnInfo(name = "details")
        val details: String?,

        @ColumnInfo(name = "wallet_type")
        val walletType: String?,

        @ColumnInfo(name = "currency")
        val currency: String?,

        @ColumnInfo(name = "date")
        val date: String,

//        @ColumnInfo(name = "current_balance")
//        val currentBalance: Long?
    )

    data class SumTransaction(
        @ColumnInfo(name = "value")
        val value: Long?,

        @ColumnInfo(name = "transaction_type")
        val transactionType: String?
    )