package com.main.netwallet.reminder

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.Observer
import com.main.netwallet.R
import com.main.netwallet.database.NetWalletDatabase
import com.main.netwallet.databinding.FragmentReminderBinding
import java.text.SimpleDateFormat
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ReminderFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ReminderFragment : Fragment() {
    // TODO: Rename and change types of parameters
    val PREF_KEY_EMAIL = "email preference"
    lateinit var sharedPreferenceEmail : SharedPreferences
    private var param1: String? = null
    private var param2: String? = null

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
        sharedPreferenceEmail = requireActivity().getSharedPreferences(PREF_KEY_EMAIL, Context.MODE_PRIVATE)
        val getEmail = sharedPreferenceEmail.getString("email_preference", null)
        val binding = DataBindingUtil.inflate<FragmentReminderBinding>(inflater, R.layout.fragment_reminder, container, false)
//        val reminderDate = binding.etSetDate.text.toString()

        val btnSetReminder = binding.btSetReminder

        btnSetReminder.setOnClickListener {
            val reminderDay = binding.etDay.text
            val reminderMonth = binding.etMonth.text
            val reminderYear = binding.etYear.text
            val date = "$reminderDay $reminderMonth $reminderYear"
            val dateFormat = SimpleDateFormat("dd MM yyyy")
            val mDate : Date = dateFormat.parse(date)
            val convertToMili = mDate.time
            val application = requireNotNull(this.activity).application
            val dataSource = NetWalletDatabase.getInstance(application).netWalletDatabaseDao
            val viewModelProvider = ReminderFragementViewModelFactory(dataSource, application, getEmail.toString(), convertToMili)
            val viewModel = ViewModelProvider(this, viewModelProvider).get(ReminderFragementViewModel::class.java)
            binding.reminder = viewModel
            val email = getEmail.toString()
            val reminderDetails = binding.etReminderDetails.text.toString()

            viewModel.addReminder(email, convertToMili, reminderDetails)

            viewModel.doneShowingToast.observe(viewLifecycleOwner, Observer{
                if(it == true){
                    Toast.makeText(context, "You Have Set A Reminder", Toast.LENGTH_LONG).show()
                }
            })
        }
        return binding.root
    }
}