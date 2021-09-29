package com.main.netwallet.monthlyTransaction

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.main.netwallet.R
import com.main.netwallet.database.NetWalletDatabase
import com.main.netwallet.databinding.FragmentSetMonthlyBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SetMonthlyFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SetMonthlyFragment : Fragment(){
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentSetMonthlyBinding
    private val PREF_KEY_EMAIL = "email preference"
    private val PREFS_KEY = "account wallet preference"
    private lateinit var emailSharedPreferences: SharedPreferences
    private lateinit var sharedPreferencesAccountWallet : SharedPreferences

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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_set_monthly,container,false)
        emailSharedPreferences = requireActivity().getSharedPreferences(PREF_KEY_EMAIL, Context.MODE_PRIVATE)
        sharedPreferencesAccountWallet = requireActivity().getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE)
        val getEmail = emailSharedPreferences.getString("email_preference", null)
        val walletTypePreference = sharedPreferencesAccountWallet.getString("wallet_type", null)
        val currencyPreference = sharedPreferencesAccountWallet.getString("currency", null)
        val bankAccountNamePreference = sharedPreferencesAccountWallet.getString("bank_account_name", null)
        val application = requireNotNull(this.activity).application
        val dataSource = NetWalletDatabase.getInstance(application).netWalletDatabaseDao
        val viewModelProvider = SetMonthlyFragmentViewModelProvider(dataSource, application, getEmail.toString())
        val viewModel = ViewModelProvider(this, viewModelProvider).get(SetMonthlyFragmentViewModel::class.java)
        val etValue = binding.etInputValue
        val spTransactionType = binding.transactionTypeSpinner
        val etDetailTransaction = binding.etDetailTransaction
        val etDate = binding.etSetDate

        ArrayAdapter.createFromResource(
            this.requireContext(),
            R.array.transaction_type,
            android.R.layout.simple_spinner_item
        )
            .also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spTransactionType.adapter = adapter
            }

        binding.btnInput.setOnClickListener {
            viewModel.setMonthlyTransaction(
                getEmail.toString(),
                etValue.text.toString().toLong(),
                spTransactionType.selectedItem.toString(),
                etDetailTransaction.text.toString(),
                walletTypePreference.toString(),
                currencyPreference.toString(),
                bankAccountNamePreference.toString(),
                etDate.text.toString().toLong()
                )

            viewModel.doneShowingToast.observe(viewLifecycleOwner, Observer {
                if(it == true){
                    Toast.makeText(context,"You have set monthly transaction", Toast.LENGTH_LONG).show()
                    findNavController().navigate(R.id.homeFragment)
                    viewModel.resetProps()
                }
            })
        }

        return binding.root
    }

}