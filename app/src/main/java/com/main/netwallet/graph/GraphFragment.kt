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
import com.main.netwallet.adapter.ShowExpensesAdapter
import com.main.netwallet.adapter.ShowTransactionAdapter
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
@RequiresApi(Build.VERSION_CODES.O)
class GraphFragment : Fragment(){
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private val PREFS_KEY = "account wallet preference"
    val PREF_KEY_EMAIL = "email preference"
    val PREFS_KEY_LOGIN = "login preference"
    lateinit var emailSharedPreferences: SharedPreferences
    lateinit var sharedPreferencesAccountWallet: SharedPreferences
    lateinit var sharedPreferencesLogin: SharedPreferences
    lateinit var binding: FragmentGraphBinding
    var from: Long = 0L
    var to: Long = 0L

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

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_graph, container, false)

        val addTransaction = binding.addTransaction

        val tvSeeFullTransaction = binding.tvToGraph

        today()

        tvSeeFullTransaction.setOnClickListener {
            findNavController().navigate(R.id.homeFragment)
        }

        addTransaction.setOnClickListener {
            findNavController().navigate(R.id.addTransactionFragment)
        }
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun today(){
        val tvToday = binding.tvToday
        val tvLastSevenDays = binding.tvLastSevenDays
        val tvLastThirtyDays = binding.tvLastThirtyDays

        val tvIncome = binding.textView6
        val tvExpenses = binding.textView10

        todayIncome()
        tvIncome.setOnClickListener { todayIncome() }
        tvExpenses.setOnClickListener { todayExpenses() }



        tvToday.setOnClickListener { false }
        tvLastSevenDays.setOnClickListener { lastSevenDays() }
        tvLastThirtyDays.setOnClickListener { lastThirtyDays() }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun lastSevenDays(){
        val tvToday = binding.tvToday
        val tvLastSevenDays = binding.tvLastSevenDays
        val tvLastThirtyDays = binding.tvLastThirtyDays

        val tvIncome = binding.textView6
        val tvExpenses = binding.textView10

        lastSevenDaysIncome()
        tvIncome.setOnClickListener { lastSevenDaysIncome() }
        tvExpenses.setOnClickListener { lastSevenDaysExpenses() }

        tvToday.setOnClickListener { today() }
        tvLastSevenDays.setOnClickListener { false }
        tvLastThirtyDays.setOnClickListener { lastThirtyDays() }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun lastThirtyDays(){
        val tvToday = binding.tvToday
        val tvLastSevenDays = binding.tvLastSevenDays
        val tvLastThirtyDays = binding.tvLastThirtyDays

        val tvIncome = binding.textView6
        val tvExpenses = binding.textView10

        lastThirtyDaysIncome()
        tvIncome.setOnClickListener { lastThirtyDaysIncome() }
        tvExpenses.setOnClickListener { lastThirtyDaysExpenses() }

        tvToday.setOnClickListener { today() }
        tvLastSevenDays.setOnClickListener { lastSevenDays() }
        tvLastThirtyDays.setOnClickListener { false }
    }

    private fun todayIncome(){
        emailSharedPreferences = requireActivity().getSharedPreferences(PREF_KEY_EMAIL, Context.MODE_PRIVATE)
        sharedPreferencesAccountWallet = requireActivity().getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE)
        sharedPreferencesLogin = requireActivity().getSharedPreferences(PREFS_KEY_LOGIN, Context.MODE_PRIVATE)
        val getEmail = emailSharedPreferences.getString("email_preference", null)
        val getWalletType = sharedPreferencesAccountWallet.getString("wallet_type", null)
        val currencyPreference = sharedPreferencesAccountWallet.getString("currency", null)
        val bankAccountNamePreference =
            sharedPreferencesAccountWallet.getString("bank_account_name", null)
        Log.e("Check Account","$getEmail, $getWalletType, $currencyPreference, $bankAccountNamePreference")
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
        val tvBalance = binding.tvBalance

        val adapter1 = ShowTransactionAdapter()

        val adapter2 = ShowExpensesAdapter()

        val today = SimpleDateFormat("dd MM yyyy").format(Date())
        val dateFormat = SimpleDateFormat("dd MM yyyy")
        val mDate = dateFormat.parse(today)
        val todayMili = mDate.time

        val tvIncome = binding.textView6
        val tvExpenses = binding.textView10

        val tvToday = binding.tvToday
        val tvLastSevenDays = binding.tvLastSevenDays
        val tvLastThirtyDays = binding.tvLastThirtyDays

        tvToday.setTextColor(resources.getColor(R.color.button_active))
        tvToday.typeface = Typeface.DEFAULT_BOLD
        tvLastSevenDays.setTextColor(Color.DKGRAY)
        tvLastSevenDays.typeface = Typeface.DEFAULT
        tvLastThirtyDays.setTextColor(Color.DKGRAY)
        tvLastThirtyDays.typeface = Typeface.DEFAULT

        to = todayMili

        viewModel.setFromAndTo(to, null)
        viewModel.resultToday()

        binding.rvShowTransaction.adapter = adapter1
        binding.rvShowExpenses.adapter = adapter2

        binding.rvShowTransaction.visibility = View.VISIBLE
        binding.rvShowExpenses.visibility = View.GONE
        tvExpenses.setTextColor(resources.getColor(R.color.black))
        tvIncome.setTextColor(resources.getColor(R.color.button_active))

        viewModel.totalTransaction.observe(viewLifecycleOwner, Observer { list ->
            list?.let {

                for (i in 0..list.size - 1) {
                    if (i == 1) {
                        val sumBalance =
                            list.get(1).value.toString().toLong() - list.get(0).value.toString()
                                .toLong()
                        tvBalance.text = sumBalance.toString()
                    } else {
//                            tvIncome.text = list.get(0).value.toString()
//                            tvExpenses.text = "0"
                        val sumBalance = list.get(0).value.toString().toLong()
                        tvBalance.text = sumBalance.toString()
                    }
                }
            }
        })

        viewModel.todayIncome.observe(viewLifecycleOwner, Observer { list ->
            list?.let {
                adapter1.data = it
            }
        })
    }

    private fun todayExpenses(){
        emailSharedPreferences = requireActivity().getSharedPreferences(PREF_KEY_EMAIL, Context.MODE_PRIVATE)
        sharedPreferencesAccountWallet = requireActivity().getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE)
        sharedPreferencesLogin = requireActivity().getSharedPreferences(PREFS_KEY_LOGIN, Context.MODE_PRIVATE)
        val getEmail = emailSharedPreferences.getString("email_preference", null)
        val getWalletType = sharedPreferencesAccountWallet.getString("wallet_type", null)
        val currencyPreference = sharedPreferencesAccountWallet.getString("currency", null)
        val bankAccountNamePreference =
            sharedPreferencesAccountWallet.getString("bank_account_name", null)
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
        val tvBalance = binding.tvBalance

        val adapter1 = ShowTransactionAdapter()

        val adapter2 = ShowExpensesAdapter()

        val today = SimpleDateFormat("dd MM yyyy").format(Date())
        val dateFormat = SimpleDateFormat("dd MM yyyy")
        val mDate = dateFormat.parse(today)
        val todayMili = mDate.time

        val tvIncome = binding.textView6
        val tvExpenses = binding.textView10

        val tvToday = binding.tvToday
        val tvLastSevenDays = binding.tvLastSevenDays
        val tvLastThirtyDays = binding.tvLastThirtyDays

        tvToday.setTextColor(resources.getColor(R.color.button_active))
        tvToday.typeface = Typeface.DEFAULT_BOLD
        tvLastSevenDays.setTextColor(Color.DKGRAY)
        tvLastSevenDays.typeface = Typeface.DEFAULT
        tvLastThirtyDays.setTextColor(Color.DKGRAY)
        tvLastThirtyDays.typeface = Typeface.DEFAULT

        to = todayMili

        viewModel.setFromAndTo(to, null)
        viewModel.resultToday()

        binding.rvShowTransaction.adapter = adapter1
        binding.rvShowExpenses.adapter = adapter2

        viewModel.totalTransaction.observe(viewLifecycleOwner, Observer { list ->
            list?.let {

                for (i in 0..list.size - 1) {
                    if (i == 1) {
                        val sumBalance =
                            list.get(1).value.toString().toLong() - list.get(0).value.toString()
                                .toLong()
                        tvBalance.text = sumBalance.toString()
                    } else {
//                            tvIncome.text = list.get(0).value.toString()
//                            tvExpenses.text = "0"
                        val sumBalance = list.get(0).value.toString().toLong()
                        tvBalance.text = sumBalance.toString()
                    }
                }
            }
        })

        binding.rvShowTransaction.visibility = View.GONE
        binding.rvShowExpenses.visibility = View.VISIBLE
        tvExpenses.setTextColor(resources.getColor(R.color.button_active))
        tvIncome.setTextColor(resources.getColor(R.color.black))
        viewModel.todayExpenses.observe(viewLifecycleOwner, Observer { list ->
            list?.let {
                adapter2.data = it
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun lastSevenDaysIncome(){
        emailSharedPreferences = requireActivity().getSharedPreferences(PREF_KEY_EMAIL, Context.MODE_PRIVATE)
        sharedPreferencesAccountWallet = requireActivity().getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE)
        sharedPreferencesLogin = requireActivity().getSharedPreferences(PREFS_KEY_LOGIN, Context.MODE_PRIVATE)
        val getEmail = emailSharedPreferences.getString("email_preference", null)
        val getWalletType = sharedPreferencesAccountWallet.getString("wallet_type", null)
        val currencyPreference = sharedPreferencesAccountWallet.getString("currency", null)
        val bankAccountNamePreference =
            sharedPreferencesAccountWallet.getString("bank_account_name", null)
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
        val tvBalance = binding.tvBalance

        val adapter1 = ShowTransactionAdapter()

        val adapter2 = ShowExpensesAdapter()

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

        val tvIncome = binding.textView6
        val tvExpenses = binding.textView10

        to = todayMili

        val lastSevenDays = LocalDate.now().minusWeeks(1)
        val formatter = DateTimeFormatter.ofPattern("dd MM yyyy")
        val dateFormatter = lastSevenDays.format(formatter)
        val simpleDateFormat = SimpleDateFormat("dd MM yyyy")
        val mDateSevenDays = simpleDateFormat.parse(dateFormatter)
        val sevenDaysInMili = mDateSevenDays.time

        from = sevenDaysInMili

        viewModel.setFromAndTo(from, to)
        viewModel.resultLastSevenDays()

        binding.rvShowTransaction.adapter = adapter1
        binding.rvShowExpenses.adapter = adapter2

        viewModel.totalTransaction.observe(viewLifecycleOwner, Observer { list ->
            list?.let {

                for (i in 0..list.size - 1) {
                    if (i == 1) {
                        val sumBalance =
                            list.get(1).value.toString().toLong() - list.get(0).value.toString()
                                .toLong()
                        tvBalance.text = sumBalance.toString()
                    } else {
//                            tvIncome.text = list.get(0).value.toString()
//                            tvExpenses.text = "0"
                        val sumBalance = list.get(0).value.toString().toLong()
                        tvBalance.text = sumBalance.toString()
                    }
                }
            }
        })
            tvExpenses.setTextColor(resources.getColor(R.color.black))
            tvIncome.setTextColor(resources.getColor(R.color.button_active))
            binding.rvShowTransaction.visibility = View.VISIBLE
            binding.rvShowExpenses.visibility = View.GONE

            viewModel.lastSevenDaysIncome.observe(viewLifecycleOwner, Observer { list ->
                list?.let {
                    adapter1.data = it
                }
            })

            Log.e("Click", "Income")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun lastSevenDaysExpenses(){
        emailSharedPreferences = requireActivity().getSharedPreferences(PREF_KEY_EMAIL, Context.MODE_PRIVATE)
        sharedPreferencesAccountWallet = requireActivity().getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE)
        sharedPreferencesLogin = requireActivity().getSharedPreferences(PREFS_KEY_LOGIN, Context.MODE_PRIVATE)
        val getEmail = emailSharedPreferences.getString("email_preference", null)
        val getWalletType = sharedPreferencesAccountWallet.getString("wallet_type", null)
        val currencyPreference = sharedPreferencesAccountWallet.getString("currency", null)
        val bankAccountNamePreference =
            sharedPreferencesAccountWallet.getString("bank_account_name", null)
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
        val tvBalance = binding.tvBalance

        val adapter1 = ShowTransactionAdapter()

        val adapter2 = ShowExpensesAdapter()

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

        val tvIncome = binding.textView6
        val tvExpenses = binding.textView10

        to = todayMili

        val lastSevenDays = LocalDate.now().minusWeeks(1)
        val formatter = DateTimeFormatter.ofPattern("dd MM yyyy")
        val dateFormatter = lastSevenDays.format(formatter)
        val simpleDateFormat = SimpleDateFormat("dd MM yyyy")
        val mDateSevenDays = simpleDateFormat.parse(dateFormatter)
        val sevenDaysInMili = mDateSevenDays.time

        from = sevenDaysInMili

        viewModel.setFromAndTo(from, to)
        viewModel.resultLastSevenDays()

        binding.rvShowTransaction.adapter = adapter1
        binding.rvShowExpenses.adapter = adapter2

        viewModel.totalTransaction.observe(viewLifecycleOwner, Observer { list ->
            list?.let {

                for (i in 0..list.size - 1) {
                    if (i == 1) {
                        val sumBalance =
                            list.get(1).value.toString().toLong() - list.get(0).value.toString()
                                .toLong()
                        tvBalance.text = sumBalance.toString()
                    } else {
//                            tvIncome.text = list.get(0).value.toString()
//                            tvExpenses.text = "0"
                        val sumBalance = list.get(0).value.toString().toLong()
                        tvBalance.text = sumBalance.toString()
                    }
                }
            }
        })
            binding.rvShowTransaction.visibility = View.GONE
            binding.rvShowExpenses.visibility = View.VISIBLE
            tvExpenses.setTextColor(resources.getColor(R.color.button_active))
            tvIncome.setTextColor(resources.getColor(R.color.black))
            viewModel.lastSevenDaysExpenses.observe(viewLifecycleOwner, Observer { list ->
                list?.let {
                    adapter2.data = it
                }
            })
            Log.e("Click", "Expenses")
    }

    private fun lastThirtyDaysIncome(){
        emailSharedPreferences = requireActivity().getSharedPreferences(PREF_KEY_EMAIL, Context.MODE_PRIVATE)
        sharedPreferencesAccountWallet = requireActivity().getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE)
        sharedPreferencesLogin = requireActivity().getSharedPreferences(PREFS_KEY_LOGIN, Context.MODE_PRIVATE)
        val getEmail = emailSharedPreferences.getString("email_preference", null)
        val getWalletType = sharedPreferencesAccountWallet.getString("wallet_type", null)
        val currencyPreference = sharedPreferencesAccountWallet.getString("currency", null)
        val bankAccountNamePreference =
            sharedPreferencesAccountWallet.getString("bank_account_name", null)
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
        val tvBalance = binding.tvBalance

        val adapter1 = ShowTransactionAdapter()

        val adapter2 = ShowExpensesAdapter()

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

        val tvIncome = binding.textView6
        val tvExpenses = binding.textView10

        from = sevenDaysInMili

        viewModel.setFromAndTo(from, to)
        viewModel.resultLastThirtyDays()

        binding.rvShowTransaction.adapter = adapter1
        binding.rvShowExpenses.adapter = adapter2

        viewModel.totalTransaction.observe(viewLifecycleOwner, Observer { list ->
            list?.let {

                for (i in 0..list.size - 1) {
                    if (i == 1) {
                        val sumBalance =
                            list.get(1).value.toString().toLong() - list.get(0).value.toString()
                                .toLong()
                        tvBalance.text = sumBalance.toString()
                    } else {
//                            tvIncome.text = list.get(0).value.toString()
//                            tvExpenses.text = "0"
                        val sumBalance = list.get(0).value.toString().toLong()
                        tvBalance.text = sumBalance.toString()
                    }
                }
            }
        })

        tvExpenses.setTextColor(resources.getColor(R.color.black))
        tvIncome.setTextColor(resources.getColor(R.color.button_active))
        binding.rvShowTransaction.visibility = View.VISIBLE
        binding.rvShowExpenses.visibility = View.GONE

        viewModel.lastThirtyDaysIncome.observe(viewLifecycleOwner, Observer { list ->
            list?.let {
                adapter1.data = it
            }
        })

    }

    private fun lastThirtyDaysExpenses(){
        emailSharedPreferences = requireActivity().getSharedPreferences(PREF_KEY_EMAIL, Context.MODE_PRIVATE)
        sharedPreferencesAccountWallet = requireActivity().getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE)
        sharedPreferencesLogin = requireActivity().getSharedPreferences(PREFS_KEY_LOGIN, Context.MODE_PRIVATE)
        val getEmail = emailSharedPreferences.getString("email_preference", null)
        val getWalletType = sharedPreferencesAccountWallet.getString("wallet_type", null)
        val currencyPreference = sharedPreferencesAccountWallet.getString("currency", null)
        val bankAccountNamePreference =
            sharedPreferencesAccountWallet.getString("bank_account_name", null)
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
        val tvBalance = binding.tvBalance

        val adapter1 = ShowTransactionAdapter()

        val adapter2 = ShowExpensesAdapter()

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

        val tvIncome = binding.textView6
        val tvExpenses = binding.textView10

        from = sevenDaysInMili

        viewModel.setFromAndTo(from, to)
        viewModel.resultLastThirtyDays()

        binding.rvShowTransaction.adapter = adapter1
        binding.rvShowExpenses.adapter = adapter2

        viewModel.totalTransaction.observe(viewLifecycleOwner, Observer { list ->
            list?.let {

                for (i in 0..list.size - 1) {
                    if (i == 1) {
                        val sumBalance =
                            list.get(1).value.toString().toLong() - list.get(0).value.toString()
                                .toLong()
                        tvBalance.text = sumBalance.toString()
                    } else {
//                            tvIncome.text = list.get(0).value.toString()
//                            tvExpenses.text = "0"
                        val sumBalance = list.get(0).value.toString().toLong()
                        tvBalance.text = sumBalance.toString()
                    }
                }
            }
        })

        binding.rvShowTransaction.visibility = View.GONE
        binding.rvShowExpenses.visibility = View.VISIBLE
        tvExpenses.setTextColor(resources.getColor(R.color.button_active))
        tvIncome.setTextColor(resources.getColor(R.color.black))
        viewModel.lastThirtyDaysExpenses.observe(viewLifecycleOwner, Observer { list ->
            list?.let {
                adapter2.data = it
            }
        })
    }
}