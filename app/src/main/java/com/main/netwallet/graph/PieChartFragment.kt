package com.main.netwallet.graph

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.main.netwallet.R
import com.main.netwallet.databinding.FragmentPieChartBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PieChartFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PieChartFragment : Fragment() {
    // TODO: Rename and change types of parameters
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
        val binding = DataBindingUtil.inflate<FragmentPieChartBinding>(inflater, R.layout.fragment_pie_chart, container, false)
        val chart : LineChart = binding.chart

        val kasus = ArrayList<Entry>()
        kasus.add(Entry(0F, 149F))
        kasus.add(Entry(1F, 113F))
        kasus.add(Entry(2F, 196F))
        kasus.add(Entry(3F, 106F))
        kasus.add(Entry(4F, 181F))
        kasus.add(Entry(5F, 218F))
        kasus.add(Entry(6F, 247F))
        kasus.add(Entry(7F, 218F))
        kasus.add(Entry(8F, 337F))
        kasus.add(Entry(9F, 219F))

        val sembuh = ArrayList<Entry>()
        sembuh.add(Entry(0F, 22F))
        sembuh.add(Entry(1F, 9F))
        sembuh.add(Entry(2F, 22F))
        sembuh.add(Entry(3F, 16F))
        sembuh.add(Entry(4F, 14F))
        sembuh.add(Entry(5F, 28F))
        sembuh.add(Entry(6F, 12F))
        sembuh.add(Entry(7F, 18F))
        sembuh.add(Entry(8F, 30F))
        sembuh.add(Entry(9F, 30F))

        val meninggal = ArrayList<Entry>()
        meninggal.add(Entry(0F, 21F))
        meninggal.add(Entry(1F, 13F))
        meninggal.add(Entry(2F, 11F))
        meninggal.add(Entry(3F, 10F))
        meninggal.add(Entry(4F, 7F))
        meninggal.add(Entry(5F, 11F))
        meninggal.add(Entry(6F, 12F))
        meninggal.add(Entry(7F, 19F))
        meninggal.add(Entry(8F, 40F))
        meninggal.add(Entry(9F, 26F))

        val kasusLineDataSet = LineDataSet(kasus, "Kasus")
        kasusLineDataSet.mode = LineDataSet.Mode.CUBIC_BEZIER
        kasusLineDataSet.color = Color.BLUE
        kasusLineDataSet.circleRadius = 5f
        kasusLineDataSet.setCircleColor(Color.BLUE)

        val sembuhLineDataSet = LineDataSet(sembuh, "Sembuh")
        sembuhLineDataSet.mode = LineDataSet.Mode.CUBIC_BEZIER
        sembuhLineDataSet.color = Color.GREEN
        sembuhLineDataSet.circleRadius = 5f
        sembuhLineDataSet.setCircleColor(Color.GREEN)

        val meninggalLineDataSet = LineDataSet(meninggal, "Meninggal")
        meninggalLineDataSet.mode = LineDataSet.Mode.CUBIC_BEZIER
        meninggalLineDataSet.color = Color.RED
        meninggalLineDataSet.circleRadius = 5f
        meninggalLineDataSet.setCircleColor(Color.RED)

//Setup Legend
        val legend = chart.legend
        legend.isEnabled = true
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP)
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER)
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL)
        legend.setDrawInside(false)

        chart.description.isEnabled = false
        chart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        chart.data = LineData(kasusLineDataSet, sembuhLineDataSet, meninggalLineDataSet)
        chart.animateXY(100, 500)

        Log.e("ArrayKasus", kasus[1].toString())

        return binding.root
    }
}