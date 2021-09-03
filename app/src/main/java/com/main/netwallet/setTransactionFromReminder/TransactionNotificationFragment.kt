package com.main.netwallet.setTransactionFromReminder

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.main.netwallet.R
import com.main.netwallet.addTransaction.AddTransactionFragmentDirections
import com.main.netwallet.addTransaction.AddTransactionFragmentViewModel
import com.main.netwallet.addTransaction.AddTransactionFragmentViewModelProvider
import com.main.netwallet.database.NetWalletDatabase
import com.main.netwallet.databinding.FragmentAddTransactionBinding
import com.main.netwallet.databinding.FragmentTransactionNotificationBinding
import androidx.lifecycle.Observer
import java.text.SimpleDateFormat
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TransactionNotificationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TransactionNotificationFragment : Fragment(), AdapterView.OnItemSelectedListener {
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
        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            findNavController().navigate(R.id.homeFragment)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val application = requireNotNull(this.activity).application
        val dataSource = NetWalletDatabase.getInstance(application).netWalletDatabaseDao
        val viewModelProvider = TransactionNotificationViewModelProvider(dataSource, application)
        val viewModel = ViewModelProvider(this, viewModelProvider).get(
            TransactionNotificationViewModel::class.java)
        val binding = DataBindingUtil.inflate<FragmentTransactionNotificationBinding>(inflater,R.layout.fragment_transaction_notification, container, false)
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
            val date : String = SimpleDateFormat("dd/MM/yyy", Locale.getDefault()).format(Date())
            val dateConvert = SimpleDateFormat("dd MM yyyy")
            val mDate : Date = dateConvert.parse(date)
            val dateToMili = mDate.time

            viewModel.addTransaction(getEmailPref.toString(), value.toLong(), transactionType, details, walletTypePreference.toString(), currencyPreference.toString(), dateToMili, bankAccountNamePreference.toString())
            viewModel.updateReminder(getEmailPref.toString())

            viewModel.doneShowingToast.observe(viewLifecycleOwner, Observer {
                if(it == true){
                    Toast.makeText(context, "Successfully Added Data", Toast.LENGTH_SHORT)
                    findNavController().navigate(R.id.homeFragment)
                    viewModel.doneNavigating()
                }
            })


        }
        return binding.root
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

}