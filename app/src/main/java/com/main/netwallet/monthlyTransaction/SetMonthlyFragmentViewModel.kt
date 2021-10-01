package com.main.netwallet.monthlyTransaction

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.main.netwallet.database.NetWalletDatabaseDao
import kotlinx.coroutines.launch

class SetMonthlyFragmentViewModel(datasource: NetWalletDatabaseDao, application: Application, email: String) : ViewModel() {

    val database = datasource

    val scheduledAt = database.getScheduledAt(email)

    val monthlyTransactionData = database.getMonthlyTransaction(email)

    val _doneShowingToast = MutableLiveData<Boolean>()
    val doneShowingToast : LiveData<Boolean>
        get() = _doneShowingToast

    val _doneNavigating = MutableLiveData<Boolean>()
    val doneNavigating : LiveData<Boolean>
        get() = _doneNavigating

    fun setMonthlyTransaction(email: String, value: Long, transactionType: String, details: String, walletType: String, currency: String, bankDetails: String, scheduledAt: Long){
        viewModelScope.launch {
            insertSetMonthlyTransaction(email, value, transactionType, details, walletType, currency, bankDetails,scheduledAt)
        }
    }

    fun addTransaction(email: String, value: Long, transactionType: String, details: String, walletType: String, currency: String, date: Long, bankDetails: String){
        viewModelScope.launch {
            insertAddTransaction(email, value, transactionType, details, walletType, currency, date, bankDetails)
            _doneNavigating.value = true
            _doneShowingToast.value = true

        }
    }

    fun delete(id: Int){
       viewModelScope.launch {
           deleteMonthly(id)
       }
    }

    private suspend fun insertAddTransaction(email: String, value: Long, transactionType: String, details: String, walletType: String, currency: String, date: Long, bankDetails: String){
        database.addTransaction(email, value, transactionType, details, walletType, currency, date, bankDetails)
    }

    private suspend fun insertSetMonthlyTransaction(email: String, value: Long, transactionType: String, details: String, walletType: String, currency: String, bankDetails: String, scheduledAt: Long){
        database.setMonthlyTransaction(email, value, transactionType, details, walletType, currency, bankDetails,scheduledAt)
        _doneShowingToast.value = true
        _doneNavigating.value = true
    }

    private suspend fun deleteMonthly(id: Int){
        database.deleteReminder(id)
    }

    fun resetProps(){
        _doneShowingToast.value = false
        _doneNavigating.value = false
    }
}