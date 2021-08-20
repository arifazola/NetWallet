package com.main.netwallet.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.main.netwallet.database.NetWalletDatabaseDao
import com.main.netwallet.database.SumTransaction
import kotlinx.coroutines.launch

class HomeFragmentViewModel(dataSource: NetWalletDatabaseDao, application: Application, email: String, walletType: String) : ViewModel() {

    val database = dataSource

    val transaction = database.getTransaction(walletType)

    val totalTransaction = database.sumTransaction(email, walletType)

}