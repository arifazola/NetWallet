package com.main.netwallet

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.main.netwallet.database.NetWalletDatabaseDao
import com.main.netwallet.register.RegisterFragmentViewModel
import com.main.netwallet.reminder.ReminderFragementViewModel
import java.lang.IllegalArgumentException

class GetDateViewModelProvider (val dataSource: NetWalletDatabaseDao, val application : Application, val email: String, val date: Long): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(GetDateViewModel::class.java)){
            return GetDateViewModel(dataSource, application, email, date) as T
        }
        throw IllegalArgumentException("Model View Can't be found")
    }

}