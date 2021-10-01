package com.main.netwallet.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.main.netwallet.database.ExpensesTransaction
import com.main.netwallet.database.IncomeTransaction
import com.main.netwallet.database.NetWalletDatabaseDao
import com.main.netwallet.database.SumTransaction
import kotlinx.coroutines.launch

class HomeFragmentViewModel(dataSource: NetWalletDatabaseDao, application: Application, email: String, walletType: String) : ViewModel() {

    val database = dataSource

    var from : Long = 0L

    var to : Long? = 0L

    val emailParam = email

    val walletParam = walletType

    val _todayExpenses = MutableLiveData<List<ExpensesTransaction>>()
    val todayExpenses : LiveData<List<ExpensesTransaction>>
        get() = _todayExpenses

    val _todayIncome = MutableLiveData<List<IncomeTransaction>>()
    val todayIncome : LiveData<List<IncomeTransaction>>
        get() = _todayIncome

    val _lastSevenDaysExpenses = MutableLiveData<List<ExpensesTransaction>>()
    val lastSevenDaysExpenses : LiveData<List<ExpensesTransaction>>
        get() = _lastSevenDaysExpenses

    val _lastSevenDaysIncome = MutableLiveData<List<IncomeTransaction>>()
    val lastSevenDaysIncome : LiveData<List<IncomeTransaction>>
        get() = _lastSevenDaysIncome

    val _lastThirtyDaysExpenses = MutableLiveData<List<ExpensesTransaction>>()
    val lastThirtyDaysExpenses : LiveData<List<ExpensesTransaction>>
        get() = _lastThirtyDaysExpenses

    val _lastThirtyDaysIncome = MutableLiveData<List<IncomeTransaction>>()
    val lastThirtyDaysIncome : LiveData<List<IncomeTransaction>>
        get() = _lastThirtyDaysIncome

//    val incomeTransaction = database.getIncomeTransactionToday(walletType, email)
//
//    val expensesTransaction = database.getExpensesTransactionToday(walletType, email)

    val totalTransaction = database.sumTransaction(email, walletType)

    suspend fun funcTodayExpenses(from: Long) : List<ExpensesTransaction>{
        val res = database.getExpensesTransactionToday(walletParam, emailParam, from)
        val getVal = res

        return getVal
    }

    suspend fun funcTodayIncome(from: Long) : List<IncomeTransaction>{
        val res = database.getIncomeTransactionToday(walletParam, emailParam, from)
        val getVal = res

        return getVal
    }

    fun resultToday(){
        viewModelScope.launch {
            _todayExpenses.value = funcTodayExpenses(from)!!
            _todayIncome.value = funcTodayIncome(from)!!
        }
    }

    suspend fun funcLastSevenDaysExpenses(from: Long, to: Long) : List<ExpensesTransaction>{
        val res = database.getExpensesTransactionLastSevenDays(walletParam, emailParam, from, to)
        val getVal = res

        return getVal
    }

    suspend fun funcLastSevenDaysIncome(from: Long, to: Long) : List<IncomeTransaction>{
        val res = database.getIncomeTransactionLastSevenDays(walletParam, emailParam, from, to)
        val getVal = res

        return getVal
    }

    fun resultLastSevenDays(){
        viewModelScope.launch {
            _lastSevenDaysExpenses.value = funcLastSevenDaysExpenses(from, to!!)!!
            _lastSevenDaysIncome.value = funcLastSevenDaysIncome(from, to!!)!!
        }
    }

    suspend fun funcLastThirtyDaysExpenses(from: Long, to: Long) : List<ExpensesTransaction>{
        val res = database.getExpensesTransactionLastThirtyDays(walletParam, emailParam, from, to)
        val getVal = res

        return getVal
    }

    suspend fun funcLastThirtyDaysIncome(from: Long, to: Long) : List<IncomeTransaction>{
        val res = database.getIncomeTransactionLastThirtyDays(walletParam, emailParam, from, to)
        val getVal = res

        return getVal
    }

    fun resultLastThirtyDays(){
        viewModelScope.launch {
            _lastThirtyDaysExpenses.value = funcLastThirtyDaysExpenses(from, to!!)!!
            _lastThirtyDaysIncome.value = funcLastThirtyDaysIncome(from, to!!)!!
        }
    }


    fun setFromAndTo(fromParam: Long, toParam: Long?){
        from = fromParam
        to = toParam
    }

//    fun resetToday(){
//        _todayExpenses.value = null
////        _todayIncome.value = null
//    }

//    fun resetLastWeek(){
//        _lastSevenDaysExpenses.value = null
//        _lastSevenDaysIncome.value = null
//
//    }

//    fun resetThirtyDays(){
//        _lastThirtyDaysExpenses.value = null
//        _lastThirtyDaysIncome.value = null
//        _sumlastThirtyDaysIncome.value = null
//    }


}