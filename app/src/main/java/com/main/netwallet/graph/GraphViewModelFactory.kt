package com.main.netwallet.graph

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.main.netwallet.database.NetWalletDatabaseDao
import com.main.netwallet.initialSetting.InitialSetttingViewModel
import java.lang.IllegalArgumentException

class GraphViewModelFactory (private val datasource: NetWalletDatabaseDao, private val application: Application, private val email: String, private val walletType: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(GraphViewModel::class.java)){
            return GraphViewModel(datasource, application, email, walletType) as T
        }else{
            throw IllegalArgumentException("View Model Cannot Be Found")
        }
    }
}