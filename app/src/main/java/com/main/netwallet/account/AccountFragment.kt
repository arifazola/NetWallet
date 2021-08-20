package com.main.netwallet.account

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.main.netwallet.R
import com.main.netwallet.database.NetWalletDatabase
import com.main.netwallet.databinding.FragmentAccountBinding
import java.util.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AccountFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AccountFragment : Fragment(), AdapterView.OnItemSelectedListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private val PREFS_KEY = "account wallet preference"
    val PREF_KEY_EMAIL = "email preference"
    lateinit var emailSharedPreferences: SharedPreferences
    lateinit var sharedPreferencesAccountWallet : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val binding = DataBindingUtil.inflate<FragmentAccountBinding>(inflater, R.layout.fragment_account, container, false)
        var etFromSpinner = binding.etFromSpinner
        sharedPreferencesAccountWallet = requireActivity().getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE)
        emailSharedPreferences = requireActivity().getSharedPreferences(PREF_KEY_EMAIL, Context.MODE_PRIVATE)
        val getEmail = emailSharedPreferences.getString("email_preference", null)
        val walletTypePreference = sharedPreferencesAccountWallet.getString("wallet_type", null)
        val currencyPreference = sharedPreferencesAccountWallet.getString("currency", null)
        val bankAccountNamePreference = sharedPreferencesAccountWallet.getString("bank_account_name", null)
//        val getWalletType = sharedPreferencesAccountWallet.getString("wallet_type", null)
//        destoryWallet()
        val application = requireNotNull(this.activity).application
        val dataSource = NetWalletDatabase.getInstance(application).netWalletDatabaseDao
        val email = "afazola@gmail.com"
        val viewModelProvider = AccountViewModelProvider(dataSource, application, getEmail.toString(), etFromSpinner.toString())
        val viewModel = ViewModelProvider(this, viewModelProvider).get(AccountViewModel::class.java)
        binding.account = viewModel

        val accountListSpinner: Spinner = binding.spAccountList

        viewModel.accountList.observe(viewLifecycleOwner, Observer { list ->
            list?.let {
//                val walletType = list.get()
                var listValue : String? = null
                val spList = arrayListOf<String>()
                for (i in 0..list.size-1) {
                    listValue = list.get(i).walletType.toString()
                    spList.add(listValue!!)
                }
//                    spList.addAll(listOf(listValue!!))
                    ArrayAdapter(
                        this.requireContext(),
                        android.R.layout.simple_spinner_item,
                        spList
                    )
                        .also { adapter ->
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                            accountListSpinner.adapter = adapter
                        }
                    Log.e("String 1", spList.toString())
                    Log.e("String 2", list.toString())

            }
        })

        accountListSpinner.onItemSelectedListener = this

        binding.tvToAddAccount.setOnClickListener {
            findNavController().navigate(AccountFragmentDirections.actionAccountFragmentToAddNewAccountFragment())
        }

        binding.btnSelectAccount.setOnClickListener {
//            destoryWallet()
            val selectedAccount = accountListSpinner.selectedItem
            saveWallet(selectedAccount.toString())
            etFromSpinner.setText(selectedAccount.toString())
//            Log.e("Account Wallet", "Spinner saved to shared preference with value $walletTypePreference")
//            Log.e("Account Wallet SP", selectedAccount)
//            viewModel.switchAccount.observe(viewLifecycleOwner, Observer { list ->
//                list?.let {
//                    val walletType = list.get(0).walletType.toString()
//                    val currency = list.get(0).currency.toString()
//                    val bankAccountName = list.get(0).bankAccountName.toString()
//
//                    switchAccount(currency, bankAccountName)
////                    findNavController().navigate(AccountFragmentDirections.actionAccountFragmentToHomeFragment())
//                    Log.e("Account Wallet DB", walletType + currency + bankAccountName)
//                    Log.e("Account Wallet DB", "Hahahhaahha")
//                }
//            })
//            findNavController().navigate(AccountFragmentDirections.actionAccountFragmentToHomeFragment())
            val intent = Intent(requireContext(), ChangingAccount::class.java)
            startActivity(intent)
        }
        Log.e("Account Wallet OnClick", walletTypePreference.toString() + currencyPreference.toString() + bankAccountNamePreference.toString())

        return binding.root
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//        val PREFS_KEY = "account wallet preference"
//        lateinit var sharedPreferencesAccountWallet : SharedPreferences
//        val selected = parent?.getItemAtPosition(position)
//        sharedPreferencesAccountWallet = requireActivity().getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE)
//        saveWallet(selected.toString())
//        val walletTypePreference = sharedPreferencesAccountWallet.getString("wallet_type", null)
//        Log.e("Account Wallet OnChange", walletTypePreference.toString())
//        Log.e("Account Wallet OnSelect", selected.toString())
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    private fun switchAccount(currency: String, bankAccountName: String){
        val editor : SharedPreferences.Editor = sharedPreferencesAccountWallet.edit()
//        editor.putString("wallet_type", walletType)
        editor.putString("currency", currency)
        editor.putString("bank_account_name", bankAccountName)
        editor.apply()
    }

//    private fun destroySwitch(){
//        val editor : SharedPreferences.Editor = sharedPreferencesAccountWallet.edit()
//        editor.remove("wallet_type")
//        editor.remove("currency")
//        editor.remove("bank_account_name")
//        editor.apply()
//    }

//    private fun destoryWallet(){
//        val editor: SharedPreferences.Editor = sharedPreferencesAccountWallet.edit()
//        editor.remove("wallet_type")
//        editor.apply()
//    }

    private fun saveWallet(walletType: String){
        val editor: SharedPreferences.Editor = sharedPreferencesAccountWallet.edit()
        editor.putString("wallet_type", walletType)
        editor.apply()
    }
}