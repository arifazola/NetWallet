package com.main.netwallet.setTransactionFromReminder

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.main.netwallet.database.NetWalletDatabaseDao
import kotlinx.coroutines.launch

class TransactionNotificationViewModel(dataSource : NetWalletDatabaseDao, application: Application) : ViewModel() {

    val database = dataSource

    private val _doneNavigating = MutableLiveData<Boolean>()
    val doneNavigating : LiveData<Boolean>
        get() = _doneNavigating

    private val _doneShowingToast = MutableLiveData<Boolean>()
    val doneShowingToast : LiveData<Boolean>
        get() = _doneShowingToast

    fun addTransaction(email: String, value: Long, transactionType: String, details: String, walletType: String, currency: String, date: String, bankDetails: String){
        viewModelScope.launch {
            insertAddTransaction(email, value, transactionType, details, walletType, currency, date, bankDetails)
            _doneNavigating.value = true
            _doneShowingToast.value = true

        }
    }

    private suspend fun insertAddTransaction(email: String, value: Long, transactionType: String, details: String, walletType: String, currency: String, date: String, bankDetails: String){
        database.addTransaction(email, value, transactionType, details, walletType, currency, date, bankDetails)
    }

    fun updateReminder(email:String){
        viewModelScope.launch {
            toUpdate(email)
            _doneNavigating.value = true
            _doneShowingToast.value = true
        }
    }

    private suspend fun toUpdate(email: String){
        database.updateReminder(email)
    }

    fun doneNavigating(){
        _doneNavigating.value = false
        _doneShowingToast.value = false
    }
}