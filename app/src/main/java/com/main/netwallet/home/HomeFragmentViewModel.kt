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

class HomeFragmentViewModel(dataSource: NetWalletDatabaseDao, application: Application) : ViewModel() {

    val database = dataSource


//    fun getSumTransaction(){
//        viewModelScope.launch {
//            val sum = database.sumTransaction()
//            Log.e("SumTransaction", sum.toString())
//        }
//    }
}