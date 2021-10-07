package com.main.netwallet.setTransactionFromReminder

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
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
import android.widget.Toast
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
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
import com.main.netwallet.MainActivity
import java.lang.IllegalStateException
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
    private val PREFS_KEY_EMAIL = "email preference"
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
            val dialog = ShowDialog()
            dialog.show(requireFragmentManager(),"dialog")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        sharedPreferencesEmail = requireActivity().getSharedPreferences(PREFS_KEY_EMAIL, Context.MODE_PRIVATE)
        val getEmailPref : String? = sharedPreferencesEmail.getString("email_preference", null)
        val application = requireNotNull(this.activity).application
        val dataSource = NetWalletDatabase.getInstance(application).netWalletDatabaseDao
        val viewModelProvider = TransactionNotificationViewModelProvider(dataSource, application)
        val viewModel = ViewModelProvider(this, viewModelProvider).get(
            TransactionNotificationViewModel::class.java)
        val binding = DataBindingUtil.inflate<FragmentTransactionNotificationBinding>(inflater,R.layout.fragment_transaction_notification, container, false)
        val transactionTypeSpinner : Spinner = binding.transactionTypeSpinner

        sharedPreferencesAccountWallet = requireActivity().getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE)
        val walletTypePreference = sharedPreferencesAccountWallet.getString("wallet_type", null)
        val currencyPreference = sharedPreferencesAccountWallet.getString("currency", null)
        val bankAccountNamePreference = sharedPreferencesAccountWallet.getString("bank_account_name", null)

        val date : String = SimpleDateFormat("dd MM yyy HH:mm:ss", Locale.getDefault()).format(Date())
        val dateConvert = SimpleDateFormat("dd MM yyyy HH:mm:ss")
        val mDate : Date = dateConvert.parse(date)
        val dateToMili = mDate.time

        viewModel.setEmailAndDate(getEmailPref.toString(), dateToMili)
        viewModel.resultGetReminderDetails()

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

//        binding.etDetailTransaction.text = viewModel.reminderDetails.value.toString()
        var id: Int? = 0

        viewModel.reminderDetails.observe(viewLifecycleOwner, Observer { list->
            list?.let {
                binding.etDetailTransaction.text = list.get(0).getReminderDetails.toString()
                id = list.get(0).id
            }
        })

        transactionTypeSpinner.onItemSelectedListener = this

        binding.btnInput.setOnClickListener {
            Log.e("Button Click Notif", "BUtton Click notif")
            val value = binding.etInputValue.text.toString()
            val transactionType = transactionTypeSpinner.selectedItem.toString()
            val details = binding.etDetailTransaction.text.toString()
            val date : String = SimpleDateFormat("dd MM yyy", Locale.getDefault()).format(Date())
            val dateConvert = SimpleDateFormat("dd MM yyyy")
            val mDate : Date = dateConvert.parse(date)
            val dateToMili = mDate.time


            viewModel.addTransaction(getEmailPref.toString(), value.toLong(), transactionType, details, walletTypePreference.toString(), currencyPreference.toString(), dateToMili, bankAccountNamePreference.toString())
            viewModel.updateReminder(getEmailPref.toString(), id!!)

            viewModel.doneShowingToast.observe(viewLifecycleOwner, Observer {
                if(it == true){
                    Toast.makeText(context, "Successfully Added Data", Toast.LENGTH_SHORT).show()
                    val intent = Intent(context, MainActivity::class.java)
                        .putExtra("FromTransactionNotif", "is_true")
                    startActivity(intent)
                    viewModel.doneNavigating()
//                    Toast.makeText(context, "Successfully Added Data", Toast.LENGTH_SHORT).show()
//                    findNavController().navigate(AddTransactionFragmentDirections.actionAddTransactionFragmentToHomeFragment())
//                    viewModel.doneNavigating()
                }
            })

        }
        return binding.root
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    class ShowDialog : DialogFragment(){
        val PREFS_KEY_EMAIL = "email preference"

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            return activity?.let {
                val sharedPreferencesEmail = requireActivity().getSharedPreferences(PREFS_KEY_EMAIL, Context.MODE_PRIVATE)
                val getEmailPref : String? = sharedPreferencesEmail.getString("email_preference", null)
                val builder = AlertDialog.Builder(it)
                val application = requireNotNull(this.activity).application
                val dataSource = NetWalletDatabase.getInstance(application).netWalletDatabaseDao
                val viewModelProvider = TransactionNotificationViewModelProvider(dataSource, application)
                val viewModel = ViewModelProvider(this, viewModelProvider).get(
                    TransactionNotificationViewModel::class.java)
                val date : String = SimpleDateFormat("dd MM yyy HH:mm:ss", Locale.getDefault()).format(Date())
                val dateConvert = SimpleDateFormat("dd MM yyyy HH:mm:ss")
                val mDate : Date = dateConvert.parse(date)
                val dateToMili = mDate.time
                viewModel.setEmailAndDate(getEmailPref.toString(), dateToMili)
                viewModel.resultGetReminderDetails()
                builder.setMessage(R.string.dialog)
                    .setPositiveButton(R.string.yes, DialogInterface.OnClickListener { dialog, id ->
                        viewModel.reminderDetails.observe(this, Observer { list->
                            list?.let {
                                val id = list.get(0).id
                                viewModel.updateReminder(getEmailPref.toString(), id!!)
                            }
                            val intent = Intent(context, MainActivity::class.java)
                                .putExtra("FromTransactionNotif", "is_true")
                            startActivity(intent)
                        })
                    })
                    .setNegativeButton(R.string.no, DialogInterface.OnClickListener { dialog, id ->
                        Toast.makeText(this.context, "No", Toast.LENGTH_LONG).show()
                    })
                builder.create()
            } ?: throw IllegalStateException("Activity Cannot be null")
        }
    }

}