package com.main.netwallet.reminder

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.main.netwallet.database.NetWalletDatabaseDao
import kotlinx.coroutines.launch

class ReminderFragementViewModel(dataSource: NetWalletDatabaseDao, application: Application, email: String) : ViewModel() {

    val database = dataSource

    val getReminderDate = database.getReminderDate(email)

    val _doneShowingToast = MutableLiveData<Boolean>()
    val doneShowingToast : LiveData<Boolean>
        get() = _doneShowingToast

    fun addReminder(email: String, reminderDate: String, reminderDetails: String){
        viewModelScope.launch {
            insertReminder(email, reminderDate, reminderDetails)
            _doneShowingToast.value = true
        }
    }

    suspend fun insertReminder(email: String, reminderDate: String, reminderDetails: String){
        database.addReminder(email, reminderDate, reminderDetails)
    }

    fun doneNavigating(){
        _doneShowingToast.value = false
    }
}