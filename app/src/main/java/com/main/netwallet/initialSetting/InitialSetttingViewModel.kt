package com.main.netwallet.initialSetting

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.main.netwallet.database.NetWalletDatabase
import com.main.netwallet.database.NetWalletDatabaseDao
import kotlinx.coroutines.launch
import java.util.*

class InitialSetttingViewModel(datasource : NetWalletDatabaseDao, application: Application) : ViewModel() {

    val database = datasource
    val getColumn = NetWalletDatabase

    private val _doneNavigating = MutableLiveData<Boolean>()
    val doneNavigating : LiveData<Boolean>
    get() = _doneNavigating

    fun insertCurrency(currency: String, hasInitialized: Boolean, email: String){
        viewModelScope.launch {
            insert(currency, hasInitialized, email)
            _doneNavigating.value = true
        }
    }

//    fun insertWalletDetails(email: String,walletType: String, balance: Long, currency: String){
//        viewModelScope.launch {
//            insertDetails(email, walletType, balance, currency)
//        }
//    }

    fun firstTransaction(email: String, value: Long, transactionType: String, details: String, walletType: String, currency: String, date: Long, bankDetails: String){
        viewModelScope.launch {
            insertFirstTransaction(email, value, transactionType, details, walletType, currency, date, bankDetails)
        }
    }

//    fun updateHasInitialized(hasInitialized: Boolean){
//        viewModelScope.launch {
//            hasInitialized(hasInitialized)
//        }
//    }

    fun getHasInitialized(email : String){
        viewModelScope.launch {
            val hasInitialized = database.getHasInitialized(email)
            if(hasInitialized?.hasInitialized == true){
                _doneNavigating.value = true
            }else{
                _doneNavigating.value = false
            }
        }
    }

    private suspend fun insert(currency: String, hasInitialized: Boolean, email: String){
        database.insertCurrency(currency, hasInitialized, email)
    }

    private suspend fun insertFirstTransaction(email: String, value: Long, transactionType: String, details: String, walletType: String, currency: String, date: Long, bankDetails: String){
        database.firstInitialize(email, value, transactionType, details, walletType, currency, date, bankDetails)
    }

//    private suspend fun insertDetails(email: String, walletType: String, balance: Long, currency: String){
//        database.insertWalletDetails(email, walletType,balance,currency)
//    }

//    private suspend fun hasInitialized(hasInitialized: Boolean){
//        database.hasInitialized(hasInitialized)
//    }

    fun doneNavigating(){
        _doneNavigating.value = false
    }
}