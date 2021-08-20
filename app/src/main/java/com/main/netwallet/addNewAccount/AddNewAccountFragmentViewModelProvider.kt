package com.main.netwallet.addNewAccount

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.main.netwallet.database.NetWalletDatabaseDao
import java.lang.IllegalArgumentException

class AddNewAccountFragmentViewModelProvider(private val datasource : NetWalletDatabaseDao, private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(AddNewAccountFragmentViewModel::class.java)){
            return AddNewAccountFragmentViewModel(datasource, application) as T
        }else{
            throw IllegalArgumentException("View Model not found")
        }
    }

}