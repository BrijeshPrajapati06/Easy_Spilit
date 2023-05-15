package com.diyantech.easysplit.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.diyantech.easysplit.R
import com.diyantech.easysplit.adapter.SplitAdapter
import com.diyantech.easysplit.databinding.FragmentSplitBinding
import com.diyantech.easysplit.modelclass.SplitClass


class SplitFragment : Fragment() {

    private lateinit var binding : FragmentSplitBinding

    var rv_split_view : RecyclerView? = null
    private var mSplitData = arrayListOf<SplitClass>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSplitBinding.inflate(inflater , container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rv_split_view = view.findViewById(R.id.rv_split_view)

        mSplitData = ArrayList()

        mSplitData.add(SplitClass(R.drawable.ig_profile,R.drawable.ig_profile,"Kirtan",R.drawable.ig_rightarrow_logo,"88.45 \$",R.drawable.ig_rightarrow_logo,"Parth"))
        mSplitData.add(SplitClass(R.drawable.ig_profile,R.drawable.ig_profile,"Brijesh",R.drawable.ig_rightarrow_logo,"88.45 \$",R.drawable.ig_rightarrow_logo,"Jigar"))
        mSplitData.add(SplitClass(R.drawable.ig_profile,R.drawable.ig_profile,"Kirtan",R.drawable.ig_rightarrow_logo,"88.45 \$",R.drawable.ig_rightarrow_logo,"Parth"))

        var adapter = SplitAdapter(mSplitData)
        rv_split_view?.adapter = adapter
        rv_split_view?.layoutManager = LinearLayoutManager(requireContext())


    }
}