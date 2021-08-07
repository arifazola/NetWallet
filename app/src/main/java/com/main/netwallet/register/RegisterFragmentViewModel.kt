package com.main.netwallet.register

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.main.netwallet.database.NetWalletDatabaseDao
import kotlinx.coroutines.launch
import javax.sql.DataSource

class RegisterFragmentViewModel(dataSource: NetWalletDatabaseDao, application: Application) : ViewModel() {

    val database = dataSource

    private val _doneNavigating = MutableLiveData<Boolean>()
    val doneNavigating : LiveData<Boolean>
    get() = _doneNavigating

    private val _doneShowingToast = MutableLiveData<Boolean>()
    val doneShowingToast : LiveData<Boolean>
    get() = _doneShowingToast

    fun onRegister(email: String, password: String, firstName: String, lastName: String){
        viewModelScope.launch {
            val getEmail = database.getEmail(email)
            if(getEmail == null){
                insert(email, password,firstName,lastName)
                _doneNavigating.value = true
                _doneShowingToast.value = false
            }else{
                _doneNavigating.value = false
                _doneShowingToast.value = true
            }
        }
    }

    private suspend fun insert(email: String, password: String, firstName: String, lastName: String){
        database.insertUser(email, password, firstName, lastName)

    }

    fun doneNavigating(){
        _doneNavigating.value = false
    }

    fun doneShowingToast(){
        _doneShowingToast.value = false
    }
}