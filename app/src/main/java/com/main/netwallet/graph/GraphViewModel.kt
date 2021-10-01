package com.main.netwallet.graph

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.main.netwallet.database.ExpensesTransaction
import com.main.netwallet.database.IncomeTransaction
import com.main.netwallet.database.NetWalletDatabaseDao
import kotlinx.coroutines.launch

class GraphViewModel(dataSource: NetWalletDatabaseDao, application: Application, email: String, walletType: String) : ViewModel() {

    val database = dataSource

    var from : Long = 0L

    var to : Long? = 0L

    val emailParam = email

    val walletParam = walletType

    val _todayExpenses = MutableLiveData<List<ExpensesTransaction?>?>()
    val todayExpenses : LiveData<List<ExpensesTransaction?>?>
        get() = _todayExpenses

    val _todayIncome = MutableLiveData<List<IncomeTransaction?>?>()
    val todayIncome : LiveData<List<IncomeTransaction?>?>
        get() = _todayIncome

    val _lastSevenDaysExpenses = MutableLiveData<List<ExpensesTransaction?>?>()
    val lastSevenDaysExpenses : LiveData<List<ExpensesTransaction?>?>
    get() = _lastSevenDaysExpenses

    val _lastSevenDaysIncome = MutableLiveData<List<IncomeTransaction?>?>()
    val lastSevenDaysIncome : LiveData<List<IncomeTransaction?>?>
        get() = _lastSevenDaysIncome

    val _lastThirtyDaysExpenses = MutableLiveData<List<ExpensesTransaction?>?>()
    val lastThirtyDaysExpenses : LiveData<List<ExpensesTransaction?>?>
        get() = _lastThirtyDaysExpenses

    val _lastThirtyDaysIncome = MutableLiveData<List<IncomeTransaction?>?>()
    val lastThirtyDaysIncome : LiveData<List<IncomeTransaction?>?>
        get() = _lastThirtyDaysIncome

    val _sumTodayIncome = MutableLiveData<List<IncomeTransaction?>?>()
    val sumTodayIncome : LiveData<List<IncomeTransaction?>?>
        get() = _sumTodayIncome

    val _sumlastSevenDaysIncome = MutableLiveData<List<IncomeTransaction?>?>()
    val sumlastSevenDaysIncome : LiveData<List<IncomeTransaction?>?>
        get() = _sumlastSevenDaysIncome

    val _sumlastThirtyDaysIncome = MutableLiveData<List<IncomeTransaction?>?>()
    val sumlastThirtyDaysIncome : LiveData<List<IncomeTransaction?>?>
        get() = _sumlastThirtyDaysIncome

    init {
        Log.e("Email Param View", emailParam)
    }

    suspend fun funcLastSevenDaysExpenses(from: Long, to: Long) : List<ExpensesTransaction?>{
        val res = database.getLastSevenDaysExpensesTransaction(walletParam, emailParam, from, to)
        val getVal = res

        return getVal
    }

    suspend fun funcLastSevenDaysIncome(from: Long, to: Long) : List<IncomeTransaction?>{
        val res = database.getLastSevenDaysIncomeTransaction(walletParam, emailParam, from, to)
        val getVal = res

        return getVal
    }

    suspend fun sumTodayIncome(from: Long) : List<IncomeTransaction>{
        val res = database.sumTodayIncome(walletParam, emailParam, from)
        val getVal = res

        return getVal
    }

    suspend fun sumLastSevenDaysIncome(from: Long, to: Long) : List<IncomeTransaction?>{
        val res = database.sumLastSevenDaysIncome(walletParam, emailParam, from, to)
        val getVal = res

        return getVal
    }

    suspend fun sumLastThirtyDaysIncome(from: Long, to: Long) : List<IncomeTransaction?>{
        val res = database.sumLastThirtyDaysIncome(walletParam, emailParam, from, to)
        val getVal = res

        return getVal
    }

    fun result(){
        viewModelScope.launch {
            _lastSevenDaysExpenses.value = funcLastSevenDaysExpenses(from, to!!)
            _lastSevenDaysIncome.value = funcLastSevenDaysIncome(from, to!!)
//            _sumlastSevenDaysExpenses.value = sumLastSevenDaysExpenses(from, to!!)
            _sumlastSevenDaysIncome.value = sumLastSevenDaysIncome(from, to!!)
        }
    }

    suspend fun funcTodayExpenses(from: Long) : List<ExpensesTransaction?>{
        val res = database.getTodayExpensesTransaction(walletParam, emailParam, from)
        val getVal = res
        return getVal
    }

    suspend fun funcTodayIncome(from: Long) : List<IncomeTransaction?>{
        val res = database.getTodayIncomeTransaction(walletParam, emailParam, from)
        val getVal = res
        return getVal
    }

    fun resultTodayTransaction(){
        viewModelScope.launch {
            _todayExpenses.value = funcTodayExpenses(from)
            _todayIncome.value = funcTodayIncome(from)
            _sumTodayIncome.value = sumTodayIncome(from)
        }
    }

    suspend fun funcLastThirtyDaysExpenses(from: Long, to: Long) : List<ExpensesTransaction?>{
        val res = database.getLastThirtyDaysExpensesTransaction(walletParam, emailParam, from, to)
        val getVal = res

        return getVal
    }

    suspend fun funcLastThirtyDaysIncome(from: Long, to: Long) : List<IncomeTransaction?>{
        val res = database.getLastThirtyDaysIncomeTransaction(walletParam, emailParam, from, to)
        val getVal = res

        return getVal
    }

    fun resultLastThirtyDaysTransaction(){
        viewModelScope.launch {
            _lastThirtyDaysExpenses.value = funcLastThirtyDaysExpenses(from, to!!)
            _lastThirtyDaysIncome.value = funcLastThirtyDaysIncome(from, to!!)
            _sumlastThirtyDaysIncome.value = sumLastThirtyDaysIncome(from, to!!)
        }
    }

    fun setFromAndTo(fromParam: Long, toParam: Long?){
        from = fromParam
        to = toParam
    }

    fun resetToday(){
        _todayExpenses.value = null
        _todayIncome.value = null
        _sumTodayIncome.value = null
    }

    fun resetLastWeek(){
        _lastSevenDaysExpenses.value = null
        _lastSevenDaysIncome.value = null
        _sumlastSevenDaysIncome.value = null
    }

    fun resetThirtyDays(){
        _lastThirtyDaysExpenses.value = null
        _lastThirtyDaysIncome.value = null
        _sumlastThirtyDaysIncome.value = null
    }


}
