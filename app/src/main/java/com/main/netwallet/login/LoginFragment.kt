package com.main.netwallet.login

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.main.netwallet.R
import com.main.netwallet.database.NetWalletDatabase
import com.main.netwallet.databinding.FragmentLoginBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    val PREFS_KEY = "login preference"
    lateinit var sharedPreferences : SharedPreferences

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
        sharedPreferences = requireActivity().getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE)
        val isLoggedIn : Boolean = sharedPreferences.getBoolean("is_loggedin", false)
        val binding = DataBindingUtil.inflate<FragmentLoginBinding>(inflater, R.layout.fragment_login, container, false)
        if(!isLoggedIn) {
            val bottomNav : BottomNavigationView? = activity?.findViewById(R.id.bn_main)
            bottomNav?.visibility
            val application = requireNotNull(this.activity).application
            val dataSource = NetWalletDatabase.getInstance(application).netWalletDatabaseDao
            val viewModelProvider = LoginFragmentViewModelProvider(dataSource, application)
            val viewModel =
                ViewModelProvider(this, viewModelProvider).get(LoginFragmentViewModel::class.java)
            binding.login = viewModel

            binding.btLogin.setOnClickListener {
                val email = binding.etEmailLogin.text.toString()
                val password = binding.etPasswordLogin.text.toString()
                viewModel.loginCheck(email, password)

                viewModel.doneNavigating.observe(viewLifecycleOwner, Observer {
                    if (it == true) {
                        findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToInitialSettingFragment())
                        viewModel.doneNavigating()
                    }
                })

                viewModel.doneShowingToast.observe(viewLifecycleOwner, Observer {
                    if (it == true) {
                        Toast.makeText(context, "Email or password is incorrect", Toast.LENGTH_LONG)
                            .show()
                        viewModel.doneShowingToast()
                    }
                })

                savePreferences()
            }

            binding.txtRegister.setOnClickListener {
                findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment())
            }
        }else{
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToInitialSettingFragment())
        }
        return binding.root

    }

    private fun savePreferences(){
        val editor : SharedPreferences.Editor = sharedPreferences.edit()
        editor.putBoolean("is_loggedin", true)
        editor.apply()
    }


}