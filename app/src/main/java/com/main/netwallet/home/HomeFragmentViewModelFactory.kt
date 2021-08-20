package com.main.netwallet.home

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.main.netwallet.database.NetWalletDatabaseDao
import java.lang.IllegalArgumentException

class HomeFragmentViewModelFactory(private val datasource: NetWalletDatabaseDao, private val application: Application, private val email: String, private val walletType: String) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(HomeFragmentViewModel::class.java)){
            return HomeFragmentViewModel(datasource, application, email, walletType) as T
        }else{
            throw IllegalArgumentException("No ViewModel Found")
        }
    }
}