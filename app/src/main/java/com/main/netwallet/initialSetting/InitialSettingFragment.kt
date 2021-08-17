package com.main.netwallet.initialSetting

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.main.netwallet.R
import com.main.netwallet.database.NetWalletDatabase
import com.main.netwallet.databinding.FragmentInitialSettingBinding
import org.w3c.dom.Text
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [InitialSettingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class InitialSettingFragment : Fragment() , AdapterView.OnItemSelectedListener{
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    val PREFS_KEY_EMAIL = "email preference"
    lateinit var sharedPreferencesEmail : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

//        val application = requireNotNull(this.activity).application
//        val dataSource = NetWalletDatabase.getInstance(application).netWalletDatabaseDao
//        val viewModelProvider = InitialSettingViewModelFactory(dataSource, application)
//        val viewModel =
//            ViewModelProvider(this, viewModelProvider).get(InitialSetttingViewModel::class.java)
//        sharedPreferencesEmail = requireActivity().getSharedPreferences(PREFS_KEY_EMAIL, Context.MODE_PRIVATE)
//        val getEmailPreferences : String? = sharedPreferencesEmail.getString("email_preference", null)
//        viewModel.getHasInitialized(getEmailPreferences!!)
//        viewModel.doneNavigating.observe(viewLifecycleOwner, Observer {
//            if(it == true){
//                findNavController().navigate(InitialSettingFragmentDirections.actionInitialSettingFragmentToHomeFragment())
//            }
//        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

            val application = requireNotNull(this.activity).application
            val dataSource = NetWalletDatabase.getInstance(application).netWalletDatabaseDao
            val viewModelProvider = InitialSettingViewModelFactory(dataSource, application)
            val viewModel =
                ViewModelProvider(this, viewModelProvider).get(InitialSetttingViewModel::class.java)
//            binding.initial = viewModel
            sharedPreferencesEmail = requireActivity().getSharedPreferences(PREFS_KEY_EMAIL, Context.MODE_PRIVATE)
            val getEmailPreferences : String? = sharedPreferencesEmail.getString("email_preference", null)

        //Checking if hasInitialized
        viewModel.getHasInitialized(getEmailPreferences!!)
        viewModel.doneNavigating.observe(viewLifecycleOwner, Observer {
            if(it == true){
//                findNavController().navigate(InitialSettingFragmentDirections.actionInitialSettingFragmentToHomeFragment())
                findNavController().navigate(R.id.homeFragment)
                viewModel.doneNavigating()
            }
        })

            val binding = DataBindingUtil.inflate<FragmentInitialSettingBinding>(inflater,R.layout.fragment_initial_setting,container,false)
            binding.initial = viewModel
            val accountTypeSpinner: Spinner = binding.spinnerAccountType

            ArrayAdapter.createFromResource(
                this.requireContext(),
                R.array.account_type,
                android.R.layout.simple_spinner_item
            )
                .also { adapter ->
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    accountTypeSpinner.adapter = adapter
                }

            val currencySpinner: Spinner = binding.spinnerCurrency

            ArrayAdapter.createFromResource(
                this.requireContext(),
                R.array.currency,
                android.R.layout.simple_spinner_item
            )
                .also { adapter ->
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    currencySpinner.adapter = adapter
                }

            accountTypeSpinner.onItemSelectedListener = this
            currencySpinner.onItemSelectedListener = this

            binding.button.setOnClickListener {
//            val accountTypeVal = binding.tvAccountType.text.toString()
//            val currencyVal = binding.tvCurrency.text.toString()
//            val balance = binding.etInputBalance.text.toString()
                val accountTypeVal = accountTypeSpinner.selectedItem.toString()
                val currencyVal = currencySpinner.selectedItem.toString()
                val balance = binding.etInputBalance.text.toString()
                val date : String = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())

                if (balance.isBlank()) {
                    Toast.makeText(context, "Please fill the empty form", Toast.LENGTH_LONG).show()
                } else {

                    viewModel.insertCurrency(currencyVal, true, getEmailPreferences.toString())
                    viewModel.firstTransaction(
                        getEmailPreferences.toString(),
                        balance.toLong(),
                        "Income",
                        "Account Opening",
                        accountTypeVal,
                        currencyVal,
                        date
                    )
//                viewModel.updateHasInitialized(true)
                    viewModel.doneNavigating.observe(viewLifecycleOwner, Observer {
                        if (it == true) {
                            findNavController().navigate(InitialSettingFragmentDirections.actionInitialSettingFragmentToHomeFragment())
                            viewModel.doneNavigating()
                        }
                    })

                }
                Log.e("email", getEmailPreferences.toString())
            }
        return binding.root
    }


    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val selected = parent?.getItemAtPosition(position)
//        val selectedSec = parent?.getItemIdAtPosition(position)
//        val accountTypeVal = requireView().findViewById<TextView>(R.id.tvAccountType)
//        val currencyTypeVal = requireView().findViewById<TextView>(R.id.tvCurrency)
//        accountTypeVal.text = selectedSec.toString()
//        currencyTypeVal.text = selected.toString()

        Log.e("Spinner", "Hello $selected")
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }


}

