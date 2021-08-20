package com.main.netwallet.account

import android.app.Application
import androidx.lifecycle.ViewModel
import com.main.netwallet.database.NetWalletDatabaseDao

class AccountViewModel(datasource: NetWalletDatabaseDao, application: Application, email: String, walletType:String) : ViewModel(){

    val database = datasource

    val accountList = database.getAccountList(email)

    val switchAccount = database.switchAccount(email, walletType)
}