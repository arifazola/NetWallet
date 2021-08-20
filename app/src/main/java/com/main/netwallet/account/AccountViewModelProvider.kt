package com.main.netwallet.account

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.main.netwallet.database.NetWalletDatabaseDao
import java.lang.IllegalArgumentException

class AccountViewModelProvider(private val datasource: NetWalletDatabaseDao, private val application: Application, private val email: String, private val walletType: String): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AccountViewModel::class.java)){
            return AccountViewModel(datasource, application, email, walletType) as T
        }else{
            throw IllegalArgumentException("No View Model Found")
        }
    }
}