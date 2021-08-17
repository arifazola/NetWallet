package com.main.netwallet.addTransaction

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.main.netwallet.database.NetWalletDatabaseDao
import java.lang.IllegalArgumentException

class AddTransactionFragmentViewModelProvider(private val dataSource : NetWalletDatabaseDao, private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(AddTransactionFragmentViewModel::class.java)){
            return AddTransactionFragmentViewModel(dataSource, application) as T
        }else{
            throw IllegalArgumentException("View Model Not Found")
        }
    }
}