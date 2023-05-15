package com.diyantech.easysplit.adapter

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.diyantech.easysplit.R
import com.diyantech.easysplit.modelclass.response.CurrencyDropListResponse
import com.diyantech.easysplit.modelclass.response.GetEventPageDataResponse

class CurrencyDropAdapter(val context: Context? = null, val dropList: List<CurrencyDropListResponse.Data>,var onItemClickListener: CurrencyDropAdapter.OnItemClickListener) :
    RecyclerView.Adapter<CurrencyDropAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.itemcurrencydialogrecycleview, parent, false)
        return CurrencyDropAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = dropList[position]

        holder.currencyName.text = model.currencyName
        holder.currencySign.text = model.currencySign

        holder.itemView.setOnClickListener(View.OnClickListener {
            onItemClickListener.onItemClick(model,position)
        })
    }

    override fun getItemCount(): Int {
        Log.d(TAG, "getItemCount: "+dropList.size)
        return dropList.size

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var currencyName : TextView = itemView.findViewById(R.id.currencyName)
        var currencySign : TextView = itemView.findViewById(R.id.currencySign)
    }

    interface OnItemClickListener {
        fun onItemClick(currencyDropListResponse: CurrencyDropListResponse.Data, position: Int)
    }
}