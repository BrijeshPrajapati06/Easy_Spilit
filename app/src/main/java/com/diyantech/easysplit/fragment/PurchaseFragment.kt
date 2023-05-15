package com.diyantech.easysplit.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.diyantech.easysplit.R
import com.diyantech.easysplit.adapter.AddUserAdapter
import com.diyantech.easysplit.adapter.PurchaseAdapter
import com.diyantech.easysplit.databinding.FragmentPurchaseBinding
import com.diyantech.easysplit.modelclass.ModelAddUser
import com.diyantech.easysplit.modelclass.PurchaseClass


class PurchaseFragment : Fragment() {

    private lateinit var binding: FragmentPurchaseBinding


    private var mData = arrayListOf<PurchaseClass>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding =FragmentPurchaseBinding.inflate(inflater,container,false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        mData = ArrayList()

        mData.add(PurchaseClass("Hotel Room","Paid by: User-1","Pay for: Everyone","348.90 \$",R.drawable.ig_profile))
        mData.add(PurchaseClass("Lunch","Paid by: User-2","Pay for: Everyone","60.90 \$",R.drawable.ig_profile))

        var adapter = PurchaseAdapter(mData)
        binding.rvView.adapter = adapter
        binding.rvView.layoutManager = LinearLayoutManager(requireContext())


    }

}