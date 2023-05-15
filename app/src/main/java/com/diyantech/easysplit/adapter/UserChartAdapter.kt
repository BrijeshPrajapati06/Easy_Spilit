package com.diyantech.easysplit.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.diyantech.easysplit.R
import com.diyantech.easysplit.modelclass.SplitClass
import com.diyantech.easysplit.modelclass.UserChartClass

class UserChartAdapter(private val mUserChartList: ArrayList<UserChartClass>) : RecyclerView.Adapter<UserChartAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.itemuserchart, parent, false)
        return UserChartAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = mUserChartList[position]

        holder.imgChartUser.setImageResource(model.imgProfile)
        holder.name?.text = model.name
        holder.totalSpend?.text = model.txSpend
        holder.mPrice?.text = model.price


    }

    override fun getItemCount(): Int {
        return mUserChartList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imgChartUser : ImageView = itemView.findViewById(R.id.imgChartUser)
        var name : TextView = itemView.findViewById(R.id.name)
        var totalSpend : TextView = itemView.findViewById(R.id.totalSpend)
        var mPrice : TextView = itemView.findViewById(R.id.mPrice)
    }
}