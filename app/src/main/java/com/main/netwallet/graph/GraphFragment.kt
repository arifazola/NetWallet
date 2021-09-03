package com.main.netwallet.graph

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
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
class GraphFragment : Fragment(), OnChartValueSelectedListener {
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

        /**
         * Bar Chart
        var chart : BarChart = binding.chart
        initBarChart(chart)
        val bar = ArrayList<BarEntry>()
        val data = ArrayList<Double>()

        for(i in 0..6){
            data.add(i * 100.0)
        }

        for(i in 0..data.size-1){
            val barEntry = BarEntry(i.toFloat(), data.get(i).toFloat())
            bar.add(barEntry)

        }

        val barDataSet = BarDataSet(bar, "Test Bar")
        initBarDataSet(barDataSet)
        val barData = BarData(barDataSet)
        chart.data = barData
        chart.invalidate()
        **/




        /**
         * Line Chart
         * */
        val chart : LineChart = binding.chart
        val expenses = ArrayList<Entry>()
        viewModel.expensesTransaction.observe(viewLifecycleOwner, Observer { list ->
            list?.let {
                for (i in 0..list.size - 1) {
                    expenses.add(Entry(i.toFloat(), list.get(i).value!!.toFloat()))
//                    kasus.add(Entry(2F, list.get(1).value!!.toFloat()))
//                    kasus.add(Entry(3F, list.get(2).value!!.toFloat()))
//                    kasus.add(Entry(4F, list.get(3).value!!.toFloat()))
//                    kasus.add(Entry(5F, list.get(4).value!!.toFloat()))
//                    kasus.add(Entry(6F, list.get(5).value!!.toFloat()))

                }
            }
            val expensesLineDataSet = LineDataSet(expenses, "Expenses")
            expensesLineDataSet.mode = LineDataSet.Mode.CUBIC_BEZIER
            expensesLineDataSet.color = Color.BLUE
            expensesLineDataSet.circleRadius = 5f
            expensesLineDataSet.setCircleColor(Color.BLUE)

            val income = ArrayList<Entry>()
            viewModel.incomeTransaction.observe(viewLifecycleOwner, Observer { list ->
                list?.let {
                    for (i in 0..list.size - 1) {
                        income.add(Entry(i.toFloat(), list.get(i).value!!.toFloat()))
//                    kasus.add(Entry(2F, list.get(1).value!!.toFloat()))
//                    kasus.add(Entry(3F, list.get(2).value!!.toFloat()))
//                    kasus.add(Entry(4F, list.get(3).value!!.toFloat()))
//                    kasus.add(Entry(5F, list.get(4).value!!.toFloat()))
//                    kasus.add(Entry(6F, list.get(5).value!!.toFloat()))

                    }
                }
                val incomeLineDataSet = LineDataSet(income, "Income")
                incomeLineDataSet.mode = LineDataSet.Mode.CUBIC_BEZIER
                incomeLineDataSet.color = Color.RED
                incomeLineDataSet.circleRadius = 5f
                incomeLineDataSet.setCircleColor(Color.RED)

                val legend = chart.legend
                legend.isEnabled = true
                legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP)
                legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER)
                legend.setOrientation(Legend.LegendOrientation.HORIZONTAL)
                legend.setDrawInside(false)

                chart.description.isEnabled = false
                chart.xAxis.position = XAxis.XAxisPosition.BOTTOM
                chart.data = LineData(expensesLineDataSet, incomeLineDataSet)
                chart.animateXY(100, 500)
            })
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

    private fun initBarDataSet(barDataSet : BarDataSet){
        barDataSet.setColor(Color.RED)
        barDataSet.formSize = 15F
        barDataSet.setDrawValues(false)
        barDataSet.valueTextSize = 12F
    }

    private fun initBarChart(chart: BarChart){
        chart.setDrawGridBackground(false)
        chart.setDrawBarShadow(false)
        chart.setDrawBorders(false)

        val description = Description()
        description.isEnabled = false
        chart.description = description

        chart.animateY(1000)
        chart.animateX(1000)

        val xAxis : XAxis = chart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.granularity = 1f
        xAxis.setDrawAxisLine(false)
        xAxis.setDrawGridLines(false)

        val letftAxis : YAxis = chart.axisLeft
        letftAxis.setDrawAxisLine(false)

        val rightAxis : YAxis = chart.axisRight
        rightAxis.setDrawAxisLine(false)

        val legend = chart.legend
        legend.form = Legend.LegendForm.CIRCLE
        legend.textSize = 11f
        legend.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
        legend.orientation = Legend.LegendOrientation.HORIZONTAL
        legend.setDrawInside(false)

    }

    override fun onValueSelected(e: Entry?, h: Highlight?) {
        Log.e("Bar Clicked", "Bar Click")
    }

    override fun onNothingSelected() {

    }
}