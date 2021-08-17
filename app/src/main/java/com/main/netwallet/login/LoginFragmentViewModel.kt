package com.main.netwallet.login

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.main.netwallet.database.NetWalletDatabaseDao
import kotlinx.coroutines.launch

class LoginFragmentViewModel(datasource : NetWalletDatabaseDao, application: Application): ViewModel() {

    val database = datasource

    private val _doneNavigating = MutableLiveData<Boolean>()
    val doneNavigating : LiveData<Boolean>
        get() = _doneNavigating

    private val _doneShowingToast = MutableLiveData<Boolean>()
    val doneShowingToast : LiveData<Boolean>
        get() = _doneShowingToast

    private val _getEmail = MutableLiveData<String>()
    val getEmail : LiveData<String>
        get() = _getEmail

    fun loginCheck(email: String, password: String){
        viewModelScope.launch {
            val checkLogin = database.loginCheck(email, password)
            if(checkLogin != null){
                _doneNavigating.value = true
                _doneShowingToast.value = false
            }else{
                _doneNavigating.value = false
                _doneShowingToast.value = true
            }
        }
    }

    fun doneNavigating(){
        _doneNavigating.value = false
    }

    fun doneShowingToast(){
        _doneShowingToast.value = false
    }
}