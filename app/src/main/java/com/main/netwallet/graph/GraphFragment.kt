package com.main.netwallet.graph

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.Utils.init
import com.main.netwallet.R
import com.main.netwallet.database.NetWalletDatabase
import com.main.netwallet.databinding.FragmentGraphBinding
import com.main.netwallet.home.HomeFragmentViewModel
import com.main.netwallet.home.HomeFragmentViewModelFactory
import java.net.Proxy
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

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
    lateinit var binding : FragmentGraphBinding
    var from : Long = 0L
    var to : Long = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
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

        binding = DataBindingUtil.inflate<FragmentGraphBinding>(inflater, R.layout.fragment_graph, container, false)

        val addTransaction = binding.addTransaction

        today()

        addTransaction.setOnClickListener{
            findNavController().navigate(R.id.addTransactionFragment)
        }

        Log.e("Init", "GraphFragment called")
        Log.e("Init", "ViewModel called")
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun today(){

        val tvToday = binding.tvToday
        val tvLastSevenDays = binding.tvLastSevenDays
        val tvLastThirtyDays = binding.tvLastThirtyDays

        tvToday.setTextColor(resources.getColor(R.color.button_active))
        tvToday.typeface = Typeface.DEFAULT_BOLD
        tvLastSevenDays.setTextColor(Color.DKGRAY)
        tvLastSevenDays.typeface = Typeface.DEFAULT
        tvLastThirtyDays.setTextColor(Color.DKGRAY)
        tvLastThirtyDays.typeface = Typeface.DEFAULT

        val today = SimpleDateFormat("dd MM yyyy").format(Date())
        val dateFormat = SimpleDateFormat("dd MM yyyy")
        val mDate = dateFormat.parse(today)
        val todayMili = mDate.time

        to = todayMili

        val lastSevenDays = LocalDate.now().minusWeeks(1)
        val formatter = DateTimeFormatter.ofPattern("dd MM yyyy")
        val dateFormatter = lastSevenDays.format(formatter)
        val simpleDateFormat = SimpleDateFormat("dd MM yyyy")
        val mDateSevenDays = simpleDateFormat.parse(dateFormatter)
        val sevenDaysInMili = mDateSevenDays.time

        from = sevenDaysInMili

        val getEmail = emailSharedPreferences.getString("email_preference", null)
        val getWalletType = sharedPreferencesAccountWallet.getString("wallet_type", null)
        val application = requireNotNull(this.activity).application
        val dataSource = NetWalletDatabase.getInstance(application).netWalletDatabaseDao
        val viewModelFactory = GraphViewModelFactory(
            dataSource,
            application,
            getEmail.toString(),
            getWalletType.toString(),
        )
        val viewModel =
            ViewModelProvider(this, viewModelFactory).get(GraphViewModel::class.java)

        viewModel.setFromAndTo(todayMili,null)

        viewModel.resultTodayTransaction()

//        viewModel.from = sevenDaysInMili
//        viewModel.to = todayMili
//
        val chart : LineChart = binding.chart
        val expenses = ArrayList<Entry>()
        viewModel.todayExpenses.observe(viewLifecycleOwner, Observer { list ->
            list?.let {
                for (i in 0..list.size - 1) {
                    expenses.add(Entry(i.toFloat(), list.get(0)!!.value!!.toFloat()))
                    Log.e("Result Expenses $i", list.get(i)!!.value!!.toString())
                }
            }
//        })
            val expensesLineDataSet = LineDataSet(expenses, "Expenses")
            expensesLineDataSet.mode = LineDataSet.Mode.CUBIC_BEZIER
            expensesLineDataSet.color = Color.BLUE
            expensesLineDataSet.circleRadius = 5f
            expensesLineDataSet.setCircleColor(Color.BLUE)

            val income = ArrayList<Entry>()
            viewModel.todayIncome.observe(viewLifecycleOwner, Observer { list ->
                list?.let {
                    for (i in 0..list.size - 1) {
                        income.add(Entry(i.toFloat(), list.get(i)!!.value!!.toFloat()))
                        Log.e("Result Income $i", list.get(i)!!.value!!.toString())
                    }
                }
//            })
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

        viewModel.resetToday()
        viewModel.resetLastWeek()
        viewModel.resetThirtyDays()

        tvToday.setOnClickListener { false }
        tvLastSevenDays.setOnClickListener { lastSevenDays() }
        tvLastThirtyDays.setOnClickListener { lastThirtyDays() }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun lastSevenDays(){
        val tvToday = binding.tvToday
        val tvLastSevenDays = binding.tvLastSevenDays
        val tvLastThirtyDays = binding.tvLastThirtyDays

        tvToday.setTextColor(Color.DKGRAY)
        tvToday.typeface = Typeface.DEFAULT
        tvLastSevenDays.setTextColor(resources.getColor(R.color.button_active))
        tvLastSevenDays.typeface = Typeface.DEFAULT_BOLD
        tvLastThirtyDays.setTextColor(Color.DKGRAY)
        tvLastThirtyDays.typeface = Typeface.DEFAULT

        val today = SimpleDateFormat("dd MM yyyy").format(Date())
        val dateFormat = SimpleDateFormat("dd MM yyyy")
        val mDate = dateFormat.parse(today)
        val todayMili = mDate.time

        to = todayMili

        val lastSevenDays = LocalDate.now().minusWeeks(1)
        val formatter = DateTimeFormatter.ofPattern("dd MM yyyy")
        val dateFormatter = lastSevenDays.format(formatter)
        val simpleDateFormat = SimpleDateFormat("dd MM yyyy")
        val mDateSevenDays = simpleDateFormat.parse(dateFormatter)
        val sevenDaysInMili = mDateSevenDays.time

        val getEmail = emailSharedPreferences.getString("email_preference", null)
        val getWalletType = sharedPreferencesAccountWallet.getString("wallet_type", null)
        val application = requireNotNull(this.activity).application
        val dataSource = NetWalletDatabase.getInstance(application).netWalletDatabaseDao
        val viewModelFactory = GraphViewModelFactory(
            dataSource,
            application,
            getEmail.toString(),
            getWalletType.toString(),
        )
        val viewModel =
            ViewModelProvider(this, viewModelFactory).get(GraphViewModel::class.java)

        from = sevenDaysInMili

        viewModel.setFromAndTo(sevenDaysInMili, todayMili)

        viewModel.result()

        val chart : LineChart = binding.chart
        val expenses = ArrayList<Entry>()
        val dateExpenses = ArrayList<String>()
        viewModel.lastSevenDaysExpenses.observe(viewLifecycleOwner, Observer { list ->
            list?.let {
                for (i in 0..list.size - 1) {
                    expenses.add(Entry(i.toFloat(), list.get(i)!!.value!!.toFloat()))
                    Log.e("Result Expenses $i", list.get(i)!!.value!!.toString())
                }
                for(i in 0..list.size - 1){
                    val dateFormatter : String = SimpleDateFormat("dd MMM").format(Date(list.get(i)!!.date!!))
                    dateExpenses.add(dateFormatter)
                }
            }
            val expensesLineDataSet = LineDataSet(expenses, "Expenses")
            expensesLineDataSet.mode = LineDataSet.Mode.CUBIC_BEZIER
            expensesLineDataSet.color = Color.BLUE
            expensesLineDataSet.circleRadius = 5f
            expensesLineDataSet.setCircleColor(Color.BLUE)

            val income = ArrayList<Entry>()
            val dateIncome = ArrayList<String>()
            viewModel.lastSevenDaysIncome.observe(viewLifecycleOwner, Observer { list ->
                list?.let {
                    for (i in 0..list.size - 1) {
                        income.add(Entry(i.toFloat(), list.get(i)!!.value!!.toFloat()))
                        Log.e("Result Income $i", list.get(i)!!.value!!.toString())
                    }
                    for(i in 0..list.size - 1){
                        val dateFormatter : String = SimpleDateFormat("dd MMM").format(Date(list.get(i)!!.date!!))
                        dateIncome.add(dateFormatter)
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

                val date = AxisDateFormatter(dateExpenses.toArray(arrayOfNulls<String>(dateExpenses.size)))
                chart.xAxis.valueFormatter = date
            })
        })

        viewModel.resetToday()
        viewModel.resetLastWeek()
        viewModel.resetThirtyDays()

        tvToday.setOnClickListener { today() }
        tvLastSevenDays.setOnClickListener { false }
        tvLastThirtyDays.setOnClickListener { lastThirtyDays() }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun lastThirtyDays(){
        val tvToday = binding.tvToday
        val tvLastSevenDays = binding.tvLastSevenDays
        val tvLastThirtyDays = binding.tvLastThirtyDays

        tvToday.setTextColor(Color.DKGRAY)
        tvToday.typeface = Typeface.DEFAULT
        tvLastSevenDays.setTextColor(Color.DKGRAY)
        tvLastSevenDays.typeface = Typeface.DEFAULT
        tvLastThirtyDays.setTextColor(resources.getColor(R.color.button_active))
        tvLastThirtyDays.typeface = Typeface.DEFAULT_BOLD

        val today = SimpleDateFormat("dd MM yyyy").format(Date())
        val dateFormat = SimpleDateFormat("dd MM yyyy")
        val mDate = dateFormat.parse(today)
        val todayMili = mDate.time

        to = todayMili

        val lastSevenDays = LocalDate.now().minusMonths(1)
        val formatter = DateTimeFormatter.ofPattern("dd MM yyyy")
        val dateFormatter = lastSevenDays.format(formatter)
        val simpleDateFormat = SimpleDateFormat("dd MM yyyy")
        val mDateSevenDays = simpleDateFormat.parse(dateFormatter)
        val sevenDaysInMili = mDateSevenDays.time

        val getEmail = emailSharedPreferences.getString("email_preference", null)
        val getWalletType = sharedPreferencesAccountWallet.getString("wallet_type", null)
        val application = requireNotNull(this.activity).application
        val dataSource = NetWalletDatabase.getInstance(application).netWalletDatabaseDao
        val viewModelFactory = GraphViewModelFactory(
            dataSource,
            application,
            getEmail.toString(),
            getWalletType.toString(),
        )
        val viewModel =
            ViewModelProvider(this, viewModelFactory).get(GraphViewModel::class.java)

        from = sevenDaysInMili

        viewModel.setFromAndTo(sevenDaysInMili, todayMili)

        viewModel.resultLastThirtyDaysTransaction()

        val chart : LineChart = binding.chart
        val expenses = ArrayList<Entry>()
        val dateExpenses = ArrayList<String>()
        viewModel.lastThirtyDaysExpenses.observe(viewLifecycleOwner, Observer { list ->
            list?.let {
                for (i in 0..list.size - 1) {
                    expenses.add(Entry(i.toFloat(), list.get(i)!!.value!!.toFloat()))
                    Log.e("Result Expenses $i", list.get(i)!!.value!!.toString())
                }
                for(i in 0..list.size - 1){
                    val dateFormatter : String = SimpleDateFormat("dd MMM").format(Date(list.get(i)!!.date!!))
                    dateExpenses.add(dateFormatter)
                }
            }
            val expensesLineDataSet = LineDataSet(expenses, "Expenses")
            expensesLineDataSet.mode = LineDataSet.Mode.CUBIC_BEZIER
            expensesLineDataSet.color = Color.BLUE
            expensesLineDataSet.circleRadius = 5f
            expensesLineDataSet.setCircleColor(Color.BLUE)

            val income = ArrayList<Entry>()
            val dateIncome = ArrayList<String>()
            viewModel.lastThirtyDaysIncome.observe(viewLifecycleOwner, Observer { list ->
                list?.let {
                    for (i in 0..list.size - 1) {
                        income.add(Entry(i.toFloat(), list.get(i)!!.value!!.toFloat()))
                        Log.e("Result Income $i", list.get(i)!!.value!!.toString())
                    }
                    for(i in 0..list.size - 1){
                        val dateFormatter : String = SimpleDateFormat("dd MMM").format(Date(list.get(i)!!.date!!))
                        dateIncome.add(dateFormatter)
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

                val date = AxisDateFormatter(dateExpenses.toArray(arrayOfNulls<String>(dateExpenses.size)))
                chart.xAxis.valueFormatter = date
            })
        })

        viewModel.resetToday()
        viewModel.resetLastWeek()
        viewModel.resetThirtyDays()

        tvToday.setOnClickListener { today() }
        tvLastSevenDays.setOnClickListener { lastSevenDays() }
        tvLastThirtyDays.setOnClickListener { false }
    }

    override fun onValueSelected(e: Entry?, h: Highlight?) {
        Log.e("Bar Clicked", "Bar Click")
    }

    override fun onNothingSelected() {

    }
}

class AxisDateFormatter(private val mValue : Array<String>) : ValueFormatter(){
    override fun getFormattedValue(value: Float): String {
        return if(value >= 0){
            if(mValue.size > value.toInt()){
                mValue[value.toInt()]
            }else{
                ""
            }
        }else{
            ""
        }
    }
}