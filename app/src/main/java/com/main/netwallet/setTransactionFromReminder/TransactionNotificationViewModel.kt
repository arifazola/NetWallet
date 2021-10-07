package com.main.netwallet.setTransactionFromReminder

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.main.netwallet.database.GetReminderDetails
import com.main.netwallet.database.NetWalletDatabaseDao
import kotlinx.coroutines.launch

class TransactionNotificationViewModel(dataSource : NetWalletDatabaseDao, application: Application) : ViewModel() {

    val database = dataSource

    var emailParam = ""

    var dateParam = 0L

//    init {
//        Log.i("TransactionNotification", "$emailParam, $dateParam")
//    }


    private val _reminderDetails = MutableLiveData<List<GetReminderDetails>>()
    val reminderDetails : LiveData<List<GetReminderDetails>>
        get() = _reminderDetails

    private val _doneNavigating = MutableLiveData<Boolean>()
    val doneNavigating : LiveData<Boolean>
        get() = _doneNavigating

    private val _doneShowingToast = MutableLiveData<Boolean>()
    val doneShowingToast : LiveData<Boolean>
        get() = _doneShowingToast

    fun addTransaction(email: String, value: Long, transactionType: String, details: String, walletType: String, currency: String, date: Long, bankDetails: String){
        viewModelScope.launch {
            insertAddTransaction(email, value, transactionType, details, walletType, currency, date, bankDetails)
            _doneNavigating.value = true
            _doneShowingToast.value = true

        }
    }

    private suspend fun insertAddTransaction(email: String, value: Long, transactionType: String, details: String, walletType: String, currency: String, date: Long, bankDetails: String){
        database.addTransaction(email, value, transactionType, details, walletType, currency, date, bankDetails)
    }

    fun updateReminder(email:String, id: Int){
        viewModelScope.launch {
            toUpdate(email, id)
            _doneNavigating.value = true
            _doneShowingToast.value = true
        }
    }

    suspend fun getReminderDetails(email: String): List<GetReminderDetails>{
        val res = database.getReminderDetails(email)
        val getRes = res
        return getRes
    }

    fun resultGetReminderDetails(){
        viewModelScope.launch {
            _reminderDetails.value = getReminderDetails(emailParam)!!
            Log.i("TransactionNotification", "$emailParam, $dateParam")
        }
    }

    fun setEmailAndDate(email: String, date: Long){
        emailParam = email
        dateParam = date
    }

    private suspend fun toUpdate(email: String, id: Int){
        database.updateReminder(email, id)
    }

    fun doneNavigating(){
        _doneNavigating.value = false
        _doneShowingToast.value = false
    }
}