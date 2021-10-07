package com.main.netwallet

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.main.netwallet.database.NetWalletDatabaseDao
import kotlinx.coroutines.launch

class GetDateViewModel (dataSource: NetWalletDatabaseDao, application: Application, email: String, date: Long) : ViewModel() {

    val database = dataSource

    val getReminderDate = database.getReminderDate(email)

}