package com.main.netwallet.reminder

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.main.netwallet.database.NetWalletDatabaseDao
import com.main.netwallet.register.RegisterFragmentViewModel
import java.lang.IllegalArgumentException

class ReminderFragementViewModelFactory(val dataSource: NetWalletDatabaseDao, val application : Application, val email: String): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ReminderFragementViewModel::class.java)){
            return ReminderFragementViewModel(dataSource, application, email) as T
        }
        throw IllegalArgumentException("Model View Can't be found")
    }

}