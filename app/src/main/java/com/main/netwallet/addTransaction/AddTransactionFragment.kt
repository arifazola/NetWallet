package com.main.netwallet.addTransaction

import android.content.Context
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
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.main.netwallet.R
import com.main.netwallet.database.NetWalletDatabase
import com.main.netwallet.databinding.FragmentAddTransactionBinding
import java.text.SimpleDateFormat
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AddTransactionFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddTransactionFragment : Fragment(), AdapterView.OnItemSelectedListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    val PREFS_KEY_EMAIL = "email preference"
    private val PREFS_KEY = "account wallet preference"
    lateinit var sharedPreferencesEmail : SharedPreferences
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
        val application = requireNotNull(this.activity).application
        val dataSource = NetWalletDatabase.getInstance(application).netWalletDatabaseDao
        val viewModelProvider = AddTransactionFragmentViewModelProvider(dataSource, application)
        val viewModel = ViewModelProvider(this, viewModelProvider).get(AddTransactionFragmentViewModel::class.java)
        val binding = DataBindingUtil.inflate<FragmentAddTransactionBinding>(inflater,R.layout.fragment_add_transaction, container, false)
        val transactionTypeSpinner : Spinner = binding.transactionTypeSpinner
        sharedPreferencesEmail = requireActivity().getSharedPreferences(PREFS_KEY_EMAIL, Context.MODE_PRIVATE)
        val getEmailPref : String? = sharedPreferencesEmail.getString("email_preference", null)

        sharedPreferencesAccountWallet = requireActivity().getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE)
        val walletTypePreference = sharedPreferencesAccountWallet.getString("wallet_type", null)
        val currencyPreference = sharedPreferencesAccountWallet.getString("currency", null)
        val bankAccountNamePreference = sharedPreferencesAccountWallet.getString("bank_account_name", null)

        ArrayAdapter.createFromResource(
            this.requireContext(),
            R.array.transaction_type,
            android.R.layout.simple_spinner_item
        )
            .also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                transactionTypeSpinner.adapter = adapter
            }

        binding.tvCurrentAccount.text = walletTypePreference.toString()

        transactionTypeSpinner.onItemSelectedListener = this

        binding.btnInput.setOnClickListener {
            val value = binding.etInputValue.text.toString()
            val transactionType = transactionTypeSpinner.selectedItem.toString()
            val details = binding.etDetailTransaction.text.toString()
            val date : String = SimpleDateFormat("dd MM yyy", Locale.getDefault()).format(Date())
            val dateConvert = SimpleDateFormat("dd MM yyyy")
            val mDate : Date = dateConvert.parse(date)
            val dateToMili = mDate.time

            viewModel.addTransaction(getEmailPref.toString(), value.toLong(), transactionType, details, walletTypePreference.toString(), currencyPreference.toString(), dateToMili, bankAccountNamePreference.toString())

            viewModel.doneShowingToast.observe(viewLifecycleOwner, Observer {
                if(it == true){
                    Toast.makeText(context, "Successfully Added Data", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(AddTransactionFragmentDirections.actionAddTransactionFragmentToHomeFragment())
                    viewModel.doneNavigating()
                }
            })
        }
        Log.e("Account Wallet", walletTypePreference.toString() + currencyPreference.toString() + bankAccountNamePreference.toString())
        return binding.root
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val selected = parent?.getItemAtPosition(position)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

}