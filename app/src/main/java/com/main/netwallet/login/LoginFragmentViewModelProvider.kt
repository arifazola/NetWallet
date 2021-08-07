package com.main.netwallet.login

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.main.netwallet.database.NetWalletDatabaseDao
import java.lang.IllegalArgumentException

class LoginFragmentViewModelProvider(private val datasource : NetWalletDatabaseDao, private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(LoginFragmentViewModel::class.java)){
            return LoginFragmentViewModel(datasource, application) as T
        }else{
            throw IllegalArgumentException("View Model cannot be found")
        }
    }
}