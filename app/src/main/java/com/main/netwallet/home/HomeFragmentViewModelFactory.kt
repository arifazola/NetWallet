package com.main.netwallet.home

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.main.netwallet.database.NetWalletDatabaseDao
import java.lang.IllegalArgumentException

class HomeFragmentViewModelFactory(private val datasource: NetWalletDatabaseDao, private val application: Application) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(HomeFragmentViewModel::class.java)){
            return HomeFragmentViewModel(datasource, application) as T
        }else{
            throw IllegalArgumentException("No ViewModel Found")
        }
    }
}