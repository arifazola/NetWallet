package com.main.netwallet.graph

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.main.netwallet.R
import com.main.netwallet.database.NetWalletDatabase
import com.main.netwallet.databinding.FragmentGraphBinding
import com.main.netwallet.home.HomeFragmentViewModel
import com.main.netwallet.home.HomeFragmentViewModelFactory

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [GraphFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GraphFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private val PREFS_KEY = "account wallet preference"
    val PREF_KEY_EMAIL = "email preference"
    val PREFS_KEY_LOGIN = "login preference"
    lateinit var emailSharedPreferences: SharedPreferences
    lateinit var sharedPreferencesAccountWallet : SharedPreferences
    lateinit var sharedPreferencesLogin : SharedPreferences

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

        sharedPreferencesAccountWallet =
            requireActivity().getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE)
        emailSharedPreferences =
            requireActivity().getSharedPreferences(PREF_KEY_EMAIL, Context.MODE_PRIVATE)
        sharedPreferencesLogin =
            requireActivity().getSharedPreferences(PREFS_KEY_LOGIN, Context.MODE_PRIVATE)

        val binding = DataBindingUtil.inflate<FragmentGraphBinding>(inflater, R.layout.fragment_graph, container, false)

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

        val chart : LineChart = binding.chart
        val kasus = ArrayList<Entry>()

        viewModel.expensesTransaction.observe(viewLifecycleOwner, Observer { list ->
            list?.let {
                for(i in 0..list.size-1){
                    kasus.add(Entry(i.toFloat(), list.get(i).value!!.toFloat()))
//                    kasus.add(Entry(2F, list.get(1).value!!.toFloat()))
//                    kasus.add(Entry(3F, list.get(2).value!!.toFloat()))
//                    kasus.add(Entry(4F, list.get(3).value!!.toFloat()))
//                    kasus.add(Entry(5F, list.get(4).value!!.toFloat()))
//                    kasus.add(Entry(6F, list.get(5).value!!.toFloat()))

                }
            }
            val kasusLineDataSet = LineDataSet(kasus, "Kasus")
            kasusLineDataSet.mode = LineDataSet.Mode.CUBIC_BEZIER
            kasusLineDataSet.color = Color.BLUE
            kasusLineDataSet.circleRadius = 5f
            kasusLineDataSet.setCircleColor(Color.BLUE)

            val legend = chart.legend
            legend.isEnabled = true
            legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP)
            legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER)
            legend.setOrientation(Legend.LegendOrientation.HORIZONTAL)
            legend.setDrawInside(false)

            chart.description.isEnabled = false
            chart.xAxis.position = XAxis.XAxisPosition.BOTTOM
            chart.data = LineData(kasusLineDataSet)
            chart.animateXY(100, 500)
        })


//        val kasus = ArrayList<Entry>()
//        kasus.add(Entry(0F, 149F))
//        kasus.add(Entry(1F, 113F))
//        kasus.add(Entry(2F, 196F))
//        kasus.add(Entry(3F, 106F))
//        kasus.add(Entry(4F, 181F))
//        kasus.add(Entry(5F, 218F))
//        kasus.add(Entry(6F, 247F))
//        kasus.add(Entry(7F, 218F))
//        kasus.add(Entry(8F, 337F))
//        kasus.add(Entry(9F, 219F))

//        val kasusLineDataSet = LineDataSet(kasus, "Kasus")
//        kasusLineDataSet.mode = LineDataSet.Mode.CUBIC_BEZIER
//        kasusLineDataSet.color = Color.BLUE
//        kasusLineDataSet.circleRadius = 5f
//        kasusLineDataSet.setCircleColor(Color.BLUE)

//Setup Legend
//        val legend = chart.legend
//        legend.isEnabled = true
//        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP)
//        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER)
//        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL)
//        legend.setDrawInside(false)
//
//        chart.description.isEnabled = false
//        chart.xAxis.position = XAxis.XAxisPosition.BOTTOM
//        chart.data = LineData(kasusLineDataSet)
//        chart.animateXY(100, 500)

        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment GraphFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            GraphFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}