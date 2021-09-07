package com.main.netwallet.home

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.main.netwallet.MainActivity
import com.main.netwallet.R
import com.main.netwallet.adapter.ShowExpensesAdapter
import com.main.netwallet.adapter.ShowTransactionAdapter
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

        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
//            val intent = Intent(activity, MainActivity::class.java)
//            startActivity(intent)
            activity?.finishAffinity()
            System.exit(0)
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

            val addTransaction = binding.addTransaction

            val tvSeeFullTransaction = binding.tvToGraph

            val adapter1 = ShowTransactionAdapter()

            val adapter2 = ShowExpensesAdapter()

            binding.rvShowTransaction.adapter = adapter1
            binding.rvShowExpenses.adapter = adapter2
//            val btnNotif = binding.btnNotif

            viewModel.totalTransaction.observe(viewLifecycleOwner, Observer { list ->
                list?.let {

                    for (i in 0..list.size - 1) {
                        if (i == 1) {

//                            tvIncome.text = list.get(1).value.toString()
//                            tvExpenses.text = list.get(0).value.toString()
                            val sumBalance = list.get(1).value.toString().toLong() - list.get(0).value.toString().toLong()
                            tvBalance.text = sumBalance.toString()
                        } else {
//                            tvIncome.text = list.get(0).value.toString()
//                            tvExpenses.text = "0"
                            val sumBalance = list.get(0).value.toString().toLong()
                            tvBalance.text = sumBalance.toString()
                        }
//                            tvIncome.text.toString().toLong() - tvExpenses.text.toString().toLong()
//                        tvBalance.text = sumBalance.toString()
//                            val sumBalance = list.get(1).value.toString().toLong() - list.get(0).value.toString().toLong()
//                        tvBalance.text = sumBalance.toString()
                    }
//                    Log.e("Wallet Home", list.get(0).value.toString())
//                Log.e("Wallet Home", list.get(1).value.toString())
                }
            })

            viewModel.incomeTransaction.observe(viewLifecycleOwner, Observer { list ->
                list?.let {
                    adapter1.data = it
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

            tvExpenses.setOnClickListener {
                binding.rvShowTransaction.visibility = View.GONE
                binding.rvShowExpenses.visibility = View.VISIBLE
                tvExpenses.setTextColor(resources.getColor(R.color.button_active))
                tvIncome.setTextColor(resources.getColor(R.color.black))
                viewModel.expensesTransaction.observe(viewLifecycleOwner, Observer { list ->
                    list?.let {
                        adapter2.data = it
                    }
                })
                Log.e("Click","Expenses")
            }

            tvIncome.setOnClickListener {
                tvExpenses.setTextColor(resources.getColor(R.color.black))
                tvIncome.setTextColor(resources.getColor(R.color.button_active))
                binding.rvShowTransaction.visibility = View.VISIBLE
                binding.rvShowExpenses.visibility = View.GONE

                viewModel.incomeTransaction.observe(viewLifecycleOwner, Observer { list ->
                    list?.let {
                        adapter1.data = it
                    }
                })

                Log.e("Click","Income")
            }

            tvSeeFullTransaction.setOnClickListener {
                findNavController().navigate(R.id.graphFragment)
            }

            addTransaction.setOnClickListener {
                findNavController().navigate(R.id.addTransactionFragment)
            }
        }

        Log.e("login", isLoggedIn.toString())
        return binding.root
    }

    private fun showIncomeTransaction(){

    }
}