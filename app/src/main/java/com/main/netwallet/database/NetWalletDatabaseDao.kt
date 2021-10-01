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
    suspend fun firstInitialize(email: String, value: Long, transactionType: String, details: String, walletType: String, currency: String, date: Long, bankDetails: String)

    //Query for adding transaction
    @Query("Insert Into transaction_history (email, value, transaction_type, details, wallet_type, currency, date, bank_account_name) Values(:email, :value, :transactionType, :details, :walletType, :currency, :date, :bankDetails)")
    suspend fun addTransaction(email: String, value: Long, transactionType: String, details: String, walletType: String, currency: String, date: Long, bankDetails: String)

    @Query("Select Sum(value) as value, transaction_type as total from transaction_history where email=:email AND wallet_type=:walletType group by transaction_type")
    fun sumTransaction(email: String, walletType: String) : LiveData<List<SumTransaction>>

    //Query to show Today Income transaction HomeFragment
    @Query("Select * from transaction_history where wallet_type=:walletType AND email=:email AND transaction_type='Income' And date=:from Order by Date Desc, ID Desc")
    suspend fun getIncomeTransactionToday(walletType: String, email:String, from: Long) : List<IncomeTransaction>

    //Query to show Today Expenses transaction Home Fragment
    @Query("Select * from transaction_history where wallet_type=:walletType AND email=:email AND transaction_type='Expenses' And date=:from Order By Date Desc, ID Desc")
    suspend fun getExpensesTransactionToday(walletType: String, email: String, from: Long) : List<ExpensesTransaction>

    //Query to show Last 7 Days Income transaction HomeFragment
    @Query("Select * from transaction_history where wallet_type=:walletType AND email=:email AND transaction_type='Income' And date Between :from AND :to Order by Date Desc, ID Desc")
    suspend fun getIncomeTransactionLastSevenDays(walletType: String, email:String, from: Long, to: Long) : List<IncomeTransaction>

    //Query to show Last 7 Days Expenses transaction Home Fragment
    @Query("Select * from transaction_history where wallet_type=:walletType AND email=:email AND transaction_type='Expenses' And date Between :from AND :to Order By Date Desc, ID Desc")
    suspend fun getExpensesTransactionLastSevenDays(walletType: String, email: String, from: Long, to: Long) : List<ExpensesTransaction>

    //Query to show Last 7 Days Income transaction HomeFragment
    @Query("Select * from transaction_history where wallet_type=:walletType AND email=:email AND transaction_type='Income' And date Between :from AND :to Order by Date Desc, ID Desc")
    suspend fun getIncomeTransactionLastThirtyDays(walletType: String, email:String, from: Long, to: Long) : List<IncomeTransaction>

    //Query to show Last 7 Days Expenses transaction Home Fragment
    @Query("Select * from transaction_history where wallet_type=:walletType AND email=:email AND transaction_type='Expenses' And date Between :from AND :to Order By Date Desc, ID Desc")
    suspend fun getExpensesTransactionLastThirtyDays(walletType: String, email: String, from: Long, to: Long) : List<ExpensesTransaction>

    //Query to show all account type
    @Query("Select wallet_type from transaction_history where details='Account Opening' AND email=:email")
    fun getAccountList(email: String): LiveData<List<AccountList>>

    //Query to switch account
    @Query("Select wallet_type, currency, bank_account_name from transaction_history where email=:email And wallet_type=:walletType Limit 1")
    fun switchAccount(email:String, walletType: String): LiveData<List<SwitchAccount>>

    //Query to input reminder
    @Query("Insert Into reminder_table (email, reminder_date, reminder_details, status) VALUES(:email, :reminderDate, :reminderDetails,'active')")
    suspend fun addReminder(email: String, reminderDate: Long, reminderDetails: String)

    //Query to get reminder date
    @Query("Select reminder_date, reminder_details from reminder_table WHERE email=:email AND reminder_date=:date AND status='active'")
    fun getReminderDate(email: String, date: Long) : LiveData<List<GetReminderDate>>

    //Query to update reminder
    @Query("Update reminder_table set status='inactive' Where email=:email")
    suspend fun updateReminder(email: String)

    //Query to get today income transaction
    @Query("Select id, email, Sum(value) as value, transaction_type, details, wallet_type, currency, date, bank_account_name from transaction_history where wallet_type=:walletType AND email =:email AND date=:from AND transaction_type ='Income' group by transaction_type, date")
    suspend fun getTodayIncomeTransaction(walletType: String, email:String, from: Long) : List<IncomeTransaction>

    //Query to get last 7 days income transaction
    @Query("Select id, email, Sum(value) as value, transaction_type, details, wallet_type, currency, date, bank_account_name from transaction_history where wallet_type=:walletType AND email =:email AND date BETWEEN :from AND :to AND transaction_type ='Income' group by transaction_type, date")
    suspend fun getLastSevenDaysIncomeTransaction(walletType: String, email:String, from: Long, to: Long) : List<IncomeTransaction>

    //Query to get last 30 days income transaction
    @Query("Select id, email, Sum(value) as value, transaction_type, details, wallet_type, currency, date, bank_account_name from transaction_history where wallet_type=:walletType AND email =:email AND date BETWEEN :from AND :to AND transaction_type ='Income' group by transaction_type, date")
    suspend fun getLastThirtyDaysIncomeTransaction(walletType: String, email:String, from: Long, to: Long) : List<IncomeTransaction>

    //Query to get today expenses transaction
    @Query("Select id, email, Sum(value) as value, transaction_type, details, wallet_type, currency, date, bank_account_name from transaction_history where wallet_type=:walletType AND email =:email AND date=:from AND transaction_type ='Expenses' group by transaction_type, date")
    suspend fun getTodayExpensesTransaction(walletType: String, email:String, from: Long) : List<ExpensesTransaction>

    //Query to get last 7 days expenses transaction
    @Query("Select id, email, Sum(value) as value, transaction_type, details, wallet_type, currency, date, bank_account_name from transaction_history where wallet_type=:walletType AND email =:email AND date BETWEEN :from AND :to AND transaction_type ='Expenses' group by transaction_type, date")
    suspend fun getLastSevenDaysExpensesTransaction(walletType: String, email:String, from: Long, to: Long) : List<ExpensesTransaction>

    //Query to get last 30 days expenses transaction
    @Query("Select id, email, Sum(value) as value, transaction_type, details, wallet_type, currency, date, bank_account_name from transaction_history where wallet_type=:walletType AND email =:email AND date BETWEEN :from AND :to AND transaction_type ='Expenses' group by transaction_type, date")
    suspend fun getLastThirtyDaysExpensesTransaction(walletType: String, email:String, from: Long, to: Long) : List<ExpensesTransaction>

    // Query to get sum today income
    @Query("Select Sum(value) as value, transaction_type from transaction_history where wallet_type=:walletType AND email =:email AND date=:from group by transaction_type")
    suspend fun sumTodayIncome(walletType: String, email: String, from: Long) : List<IncomeTransaction>

    //Query to get sum last 7 days income
    @Query("Select Sum(value) as value, transaction_type from transaction_history where wallet_type=:walletType AND email =:email AND date BETWEEN :from AND :to group by transaction_type")
    suspend fun sumLastSevenDaysIncome(walletType: String, email:String, from: Long, to: Long) : List<IncomeTransaction>

    //Query to get sum last 30 days income
    @Query("Select Sum(value) as value, transaction_type from transaction_history where wallet_type=:walletType AND email =:email AND date BETWEEN :from AND :to group by transaction_type")
    suspend fun sumLastThirtyDaysIncome(walletType: String, email:String, from: Long, to: Long) : List<IncomeTransaction>

    // Query to get sum today expenses
    @Query("Select id, email, Sum(value) as value, transaction_type, details, wallet_type, currency, date, bank_account_name from transaction_history where wallet_type=:walletType AND email =:email AND date=:from AND transaction_type ='Expenses' group by transaction_type")
    suspend fun sumTodayExpenses(walletType: String, email: String, from: Long) : List<ExpensesTransaction>

    //Query to get sum last 7 days expenses
    @Query("Select Sum(value) as value from transaction_history where wallet_type=:walletType AND email =:email AND date BETWEEN :from AND :to AND transaction_type ='Expenses' group by transaction_type")
    suspend fun sumLastSevenDaysExpenses(walletType: String, email:String, from: Long, to: Long) : List<ExpensesTransaction>

    //Query to get sum last 30 days expenses
    @Query("Select id, email, Sum(value) as value, transaction_type, details, wallet_type, currency, date, bank_account_name from transaction_history where wallet_type=:walletType AND email =:email AND date BETWEEN :from AND :to AND transaction_type ='Expenses' group by transaction_type")
    suspend fun sumLastThirtyDaysExpenses(walletType: String, email:String, from: Long, to: Long) : List<ExpensesTransaction>

    //Query to add monthly transaction
    @Query("Insert into set_monthly_transaction (email, value, transaction_type, details, wallet_type, currency, bank_account_name, scheduled_at) Values (:email,:value,:transactionType,:details,:walletType,:currency,:bank_account_name,:scheduledAt)")
    suspend fun setMonthlyTransaction(email: String, value: Long, transactionType: String, details: String, walletType: String, currency: String, bank_account_name: String, scheduledAt: Long)

    //Query to get date scheduled_at from set_montly_transaction
    @Query("Select id, email, value, transaction_type, details, wallet_type, currency, bank_account_name, scheduled_at from set_monthly_transaction where email = :email")
    fun getScheduledAt(email: String) : LiveData<List<ScheduledAt>>

    //Query to get all data from set_monthly_transaction
    @Query("Select id, email, value, transaction_type, details, wallet_type, currency, bank_account_name from set_monthly_transaction where email = :email")
    fun getMonthlyTransaction(email: String) : LiveData<List<GetMonthlyData>>

    @Query("Delete from set_monthly_transaction where id=:id")
    suspend fun deleteReminder(id: Int)
}