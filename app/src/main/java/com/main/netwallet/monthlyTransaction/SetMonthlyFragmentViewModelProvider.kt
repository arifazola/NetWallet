package com.main.netwallet.monthlyTransaction

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.main.netwallet.database.NetWalletDatabaseDao
import java.lang.IllegalArgumentException

class SetMonthlyFragmentViewModelProvider(private val datasource: NetWalletDatabaseDao, private val application: Application, private val email: String): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(SetMonthlyFragmentViewModel::class.java)){
            return SetMonthlyFragmentViewModel(datasource,application, email) as T
        }else{
            throw IllegalArgumentException("No ViewModel Avail")
        }
    }
}