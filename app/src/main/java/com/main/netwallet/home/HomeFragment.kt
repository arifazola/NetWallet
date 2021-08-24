package com.main.netwallet.home

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.main.netwallet.MainActivity
import com.main.netwallet.R
import com.main.netwallet.database.NetWalletDatabase
import com.main.netwallet.databinding.FragmentHomeBinding
import kotlin.math.log

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private val PREFS_KEY = "account wallet preference"
    val PREF_KEY_EMAIL = "email preference"
    val PREFS_KEY_LOGIN = "login preference"
    lateinit var emailSharedPreferences: SharedPreferences
    lateinit var sharedPreferencesAccountWallet : SharedPreferences
    lateinit var sharedPreferencesLogin : SharedPreferences
//    private val PREFS_KEY = "login preference"
//    private val PREFS_KEY_EMAIL = "email preference"
//    lateinit var sharedPreferences: SharedPreferences
//    lateinit var emailSharedPreferences: SharedPreferences

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
//        sharedPreferences = requireActivity().getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE)
//        emailSharedPreferences = requireActivity().getSharedPreferences(PREFS_KEY_EMAIL, Context.MODE_PRIVATE)
        sharedPreferencesAccountWallet =
            requireActivity().getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE)
        emailSharedPreferences =
            requireActivity().getSharedPreferences(PREF_KEY_EMAIL, Context.MODE_PRIVATE)
        sharedPreferencesLogin =
            requireActivity().getSharedPreferences(PREFS_KEY_LOGIN, Context.MODE_PRIVATE)
        val isLoggedIn: Boolean = sharedPreferencesLogin.getBoolean("is_loggedin", false)
        val binding = DataBindingUtil.inflate<FragmentHomeBinding>(
            inflater,
            R.layout.fragment_home,
            container,
            false
        )
        if (isLoggedIn == false) {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToLoginFragment())
        } else {

            val getEmail = emailSharedPreferences.getString("email_preference", null)
            val getWalletType = sharedPreferencesAccountWallet.getString("wallet_type", null)
            val currencyPreference = sharedPreferencesAccountWallet.getString("currency", null)
            val bankAccountNamePreference =
                sharedPreferencesAccountWallet.getString("bank_account_name", null)
//        val walletTypePreference = sharedPreferencesAccountWallet.getString("wallet_type", null)
//        val currencyPreference = sharedPreferencesAccountWallet.getString("currency", null)
//        val bankAccountNamePreference = sharedPreferencesAccountWallet.getString("bank_account_name", null)
            val application = requireNotNull(this.activity).application
            val dataSource = NetWalletDatabase.getInstance(application).netWalletDatabaseDao
            val viewModelFactory = HomeFragmentViewModelFactory(
                dataSource,
                application,
                getEmail.toString(),
                getWalletType.toString()
            )
            val viewModel =
                ViewModelProvider(this, viewModelFactory).get(HomeFragmentViewModel::class.java)
//            val tvIncome = binding.tvIncome
//            val tvExpenses = binding.tvExpenses
            val tvBalance = binding.tvBalance
//            val btnNotif = binding.btnNotif

            viewModel.totalTransaction.observe(viewLifecycleOwner, Observer { list ->
                list?.let {

                    for (i in 0..list.size - 1) {
                        if (i == 1) {

//                            tvIncome.text = list.get(1).value.toString()
//                            tvExpenses.text = list.get(0).value.toString()
                        } else {
//                            tvIncome.text = list.get(0).value.toString()
//                            tvExpenses.text = "0"
                        }
//                            tvIncome.text.toString().toLong() - tvExpenses.text.toString().toLong()
//                        tvBalance.text = sumBalance.toString()
                            val sumBalance = list.get(1).value.toString().toLong() - list.get(0).value.toString().toLong()
                        tvBalance.text = sumBalance.toString()
                    }
//                    Log.e("Wallet Home", list.get(0).value.toString())
//                Log.e("Wallet Home", list.get(1).value.toString())
                }
            })

            Log.e(
                "Account Wallet Home",
                getWalletType.toString() + currencyPreference.toString() + bankAccountNamePreference.toString()
            )

//            btnNotif.setOnClickListener {
//                (activity as MainActivity?)!!.notif()
//            }

            val tvIncome = binding.textView6
            val tvExpenses = binding.textView10
            val tvTransactionList = binding.tvTransactionList

            tvExpenses.setOnClickListener {
                tvTransactionList.text=""
                tvIncome.setTextColor(resources.getColor(R.color.black))
                tvExpenses.setTextColor(resources.getColor(R.color.button_active))
                viewModel.expensesTransaction.observe(viewLifecycleOwner, Observer { list ->
                    list?.let {
                        for(i in 0..list.size-1){
                            tvTransactionList.append("," + list.get(i).value.toString())
                        }
                    }
                })
            }

            tvIncome.setOnClickListener {
                tvTransactionList.text = ""
                tvIncome.setTextColor(resources.getColor(R.color.button_active))
                tvExpenses.setTextColor(resources.getColor(R.color.black))
                viewModel.incomeTransaction.observe(viewLifecycleOwner, Observer { list ->
                    list?.let {
                        for(i in 0..list.size-1){
                            tvTransactionList.append("," + list.get(i).value.toString())
                        }
                    }
                })
            }
        }

        Log.e("login", isLoggedIn.toString())
        return binding.root
    }

    private fun showIncomeTransaction(){

    }
}