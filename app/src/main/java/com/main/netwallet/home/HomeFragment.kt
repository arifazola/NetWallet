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
        val binding = DataBindingUtil.inflate<FragmentHomeBinding>(inflater, R.layout.fragment_home, container, false)
        val application = requireNotNull(this.activity).application
        val dataSource = NetWalletDatabase.getInstance(application).netWalletDatabaseDao
        val viewModelFactory = HomeFragmentViewModelFactory(dataSource, application)
        val viewModel = ViewModelProvider(this, viewModelFactory).get(HomeFragmentViewModel::class.java)
        val tvIncome = binding.tvIncome
        val tvExpenses = binding.tvExpenses

        viewModel.totalTransaction.observe(viewLifecycleOwner, Observer { list ->
            list?.let {

//                for(i in 0.. list.size-1){
                    tvIncome.text = list.get(1).value.toString()
                    tvExpenses.text = list.get(0).value.toString()
//                }
            }
        })

//        binding.btnLogout.setOnClickListener {
//            logout()
//            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToLoginFragment())
//        }

//        binding.button2.setOnClickListener {
//            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToAddTransactionFragment2())
//        }
        return binding.root
    }

//    private fun logout(){
////        sharedPreferences = requireActivity().getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE)
//        val editor : SharedPreferences.Editor = sharedPreferences.edit()
//        editor.clear()
//        editor.apply()
//
//        val editor2 : SharedPreferences.Editor = emailSharedPreferences.edit()
//        editor2.clear()
//        editor2.apply()
//    }


}