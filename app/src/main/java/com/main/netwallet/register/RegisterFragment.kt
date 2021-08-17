package com.main.netwallet.register

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
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.main.netwallet.R
import com.main.netwallet.database.NetWalletDatabase
import com.main.netwallet.database.NetWalletDatabaseDao
import com.main.netwallet.databinding.FragmentRegisterBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RegisterFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RegisterFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private val PREFS_KEY_EMAIL = "email preference"
    lateinit var sharedPreferences: SharedPreferences

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
        sharedPreferences = requireActivity().getSharedPreferences(PREFS_KEY_EMAIL, Context.MODE_PRIVATE)
        val binding = DataBindingUtil.inflate<FragmentRegisterBinding>(inflater,R.layout.fragment_register, container, false)
        val application = requireNotNull(this.activity).application
        val dataSource = NetWalletDatabase.getInstance(application).netWalletDatabaseDao
        val viewModelFactory = RegisterFragmentViewModelFactory(dataSource, application)
        val viewModel = ViewModelProvider(this, viewModelFactory).get(RegisterFragmentViewModel::class.java)

        binding.user = viewModel

        binding.buttonRegister.setOnClickListener {
            val email = binding.etEmailRegister.text.toString()
            val password = binding.etPasswordRegister.text.toString()
            val firstName = binding.etFirstNameRegister.text.toString()
            val lastName = binding.etLastNameRegister.text.toString()

            if(email.isBlank() || password.isBlank() || firstName.isBlank() || lastName.isBlank()){
                Toast.makeText(context, "You need to fill all the data required", Toast.LENGTH_LONG).show()
            }else {

                viewModel.onRegister(email, password, firstName, lastName)

                viewModel.doneShowingToast.observe(viewLifecycleOwner, Observer {
                    if (it == true) {
                        Toast.makeText(
                            context,
                            "Email has already been registered",
                            Toast.LENGTH_LONG
                        ).show()
                        viewModel.doneShowingToast()
                    }
                })

                viewModel.doneNavigating.observe(viewLifecycleOwner, Observer {
                    if (it == true) {
                        findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToInitialSettingFragment())
                        viewModel.doneNavigating()
                    }
                })

                setEmailSharedPreference(email)
            }
        }
        return binding.root
    }

    private fun setEmailSharedPreference(email: String){
        val editor : SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString("email_preference", email)
        editor.apply()
    }


}