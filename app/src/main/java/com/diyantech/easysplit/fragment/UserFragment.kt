package com.diyantech.easysplit.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.diyantech.easysplit.R
import com.diyantech.easysplit.adapter.SplitAdapter
import com.diyantech.easysplit.adapter.UserChartAdapter
import com.diyantech.easysplit.databinding.FragmentSplitBinding
import com.diyantech.easysplit.databinding.FragmentUserBinding
import com.diyantech.easysplit.modelclass.UserChartClass
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry


class UserFragment : Fragment() {

    private lateinit var binding : FragmentUserBinding
    private var mUserChartData = arrayListOf<UserChartClass>()

//    var pieData: PieData? = null
//    var pieDataSet: PieDataSet? = null
////    var pieEntries: ArrayList<String>? = null
////    var PieEntryLabels: ArrayList<String>? = null
//    var pieEntries: ArrayList<PieEntry>? = null
//    var PieEntryLabels: ArrayList<PieEntry>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserBinding.inflate(inflater , container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        mUserChartData = ArrayList()

        mUserChartData.add(UserChartClass(R.drawable.ig_profile,"Parth Patel","Total spend: 1800/-$","800.50 $"))
        mUserChartData.add(UserChartClass(R.drawable.ig_profile,"Brijesh Prajapati","Total spend: 180/-$","800.50 $"))
        mUserChartData.add(UserChartClass(R.drawable.ig_profile,"Patel deep","Total spend: 1600/-$","800.50 $"))
        mUserChartData.add(UserChartClass(R.drawable.ig_profile,"Parth Patel","Total spend: 1700/-$","800.50 $"))

        var adapter = UserChartAdapter(mUserChartData)
        binding.mRvView.adapter = adapter
        binding.mRvView.layoutManager = LinearLayoutManager(requireContext())


        binding.pieChart.setUsePercentValues(true)
        binding.pieChart.description?.isEnabled = false
        binding.pieChart.offsetLeftAndRight(15)
        binding.pieChart.offsetTopAndBottom(10)
        binding.pieChart.setExtraOffsets(5F, 10F, 5F, 5F)

        binding.pieChart.dragDecelerationFrictionCoef = 0.95f

        binding.pieChart.isDrawHoleEnabled = true

        binding.pieChart.setHoleColor(Color.WHITE)
        binding.pieChart.transparentCircleRadius = 61f

        var yValues: ArrayList<PieEntry> = ArrayList()

        yValues.add(PieEntry(34f, "India"))
        yValues.add(PieEntry(23f, "USA"))
        yValues.add(PieEntry(14f, "Uk"))
        yValues.add(PieEntry(35f, "USA"))
        yValues.add(PieEntry(40f, "Russia"))
        yValues.add(PieEntry(23f, "Japan"))


        var dataSet = PieDataSet(yValues, "Countries")
        dataSet.sliceSpace = 3f
        dataSet.selectionShift = 5f
        dataSet.setColors(Color.RED, Color.BLUE, Color.YELLOW, Color.GREEN, Color.BLACK);
//       dataSet.setColor(Color.GREEN)

        var data = PieData(dataSet)
        data.setValueTextSize(10f)
        data.setValueTextColor(Color.YELLOW)

        binding.pieChart.data = data



    }

}