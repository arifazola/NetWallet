package com.main.netwallet.register

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.main.netwallet.database.NetWalletDatabaseDao
import java.lang.IllegalArgumentException

class RegisterFragmentViewModelFactory(private val dataSource: NetWalletDatabaseDao, private val application : Application): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(RegisterFragmentViewModel::class.java)){
            return RegisterFragmentViewModel(dataSource, application) as T
        }
            throw IllegalArgumentException("Model View Can't be found")
    }

}