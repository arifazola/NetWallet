package com.main.netwallet.initialSetting

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.main.netwallet.database.NetWalletDatabaseDao
import kotlinx.coroutines.launch

class InitialSetttingViewModel(datasource : NetWalletDatabaseDao, application: Application) : ViewModel() {

    val database = datasource

    private val _doneNavigating = MutableLiveData<Boolean>()
    val doneNavigating : LiveData<Boolean>
    get() = _doneNavigating

    fun insertCurrency(currency: String){
        viewModelScope.launch {
            insert(currency)
            _doneNavigating.value = true
        }
    }

    fun insertWalletDetails(walletType: String, balance: Long, currency: String){
        viewModelScope.launch {
            insertDetails(walletType, balance, currency)
        }
    }

    private suspend fun insert(currency: String){
        database.insertCurrency(currency)
    }

    private suspend fun insertDetails(walletType: String, balance: Long, currency: String){
        database.insertWalletDetails(walletType,balance,currency)
    }

    fun doneNavigating(){
        _doneNavigating.value = false
    }
}