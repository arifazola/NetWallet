package com.main.netwallet.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.main.netwallet.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        val reminder : Preference? = findPreference<Preference>("reminder")

        reminder?.setOnPreferenceClickListener {
            findNavController().navigate(R.id.reminderFragment)
            true
        }
    }

//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//
//        val reminder = findView
//        return super.onCreateView(inflater, container, savedInstanceState)
//    }
}