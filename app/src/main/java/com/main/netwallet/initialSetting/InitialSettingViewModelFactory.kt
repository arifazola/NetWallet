package com.main.netwallet.initialSetting

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.main.netwallet.database.NetWalletDatabaseDao
import java.lang.IllegalArgumentException

class InitialSettingViewModelFactory(private val datasource: NetWalletDatabaseDao, private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(InitialSetttingViewModel::class.java)){
            return InitialSetttingViewModel(datasource, application) as T
        }else{
            throw IllegalArgumentException("View Model Cannot Be Found")
        }
    }
}