package com.main.netwallet.setTransactionFromReminder

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.main.netwallet.addTransaction.AddTransactionFragmentViewModel
import com.main.netwallet.database.NetWalletDatabaseDao
import java.lang.IllegalArgumentException

class TransactionNotificationViewModelProvider(private val dataSource : NetWalletDatabaseDao, private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(TransactionNotificationViewModel::class.java)){
            return TransactionNotificationViewModel(dataSource, application) as T
        }else{
            throw IllegalArgumentException("View Model Not Found")
        }
    }
}