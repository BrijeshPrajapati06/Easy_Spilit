package com.diyantech.easysplit.adapter

import android.annotation.SuppressLint
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
import com.diyantech.easysplit.modelclass.response.EventCreateResponse
import com.diyantech.easysplit.modelclass.response.GetEventPageDataResponse

class EventAdapter(
    var context: Context? = null,
    var onItemClickListener: OnItemClickListener,
    val mList: ArrayList<GetEventPageDataResponse.Data.Result>):
    RecyclerView.Adapter<EventAdapter.ViewHolder>() {

    var eventCreateResponse : EventCreateResponse? = null



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_recycleview, parent, false)
        return ViewHolder(view)
    }



    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = mList[position]
        var created = eventCreateResponse?.data?.createdAt
        val prefe = context?.getSharedPreferences("PREF", Context.MODE_PRIVATE)

        val cSign = prefe?.getString("currencySign", "")

        holder.ladakh_name.text = model.eventName
        holder.description.text = model.description
        holder.tvCreated.text = "Created at 07/12/22"
        holder.tvPrice.text = model.totalSpendAmount.toString() + "/-"
        holder.tvCurreSign.text = cSign
        Log.d(TAG, "onBindViewHolder: "+ cSign )
        holder.itemView.setOnClickListener(View.OnClickListener {
            onItemClickListener.onItemClick(model,position)
        })


    }

    override fun getItemCount(): Int {
        Log.d(TAG, "getItemCountEvent: "+mList.size)
        return mList.size
    }

    fun addList(dataList: List<GetEventPageDataResponse.Data.Result>) {
        if (dataList != null){
            mList.addAll(dataList)
            notifyDataSetChanged()
        }
    }

//    @SuppressLint("NotifyDataSetChanged")
//    fun addList(dataList: List<GetEventPageDataResponse.Data.Result>) {
//        if (dataList != null) {
//            mList.(dataList)
//            notifyDataSetChanged()
//        }
//    }



    interface OnItemClickListener{

        fun onItemClick(getEventDataResponse: GetEventPageDataResponse.Data.Result , position: Int)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ladakh_name: TextView = itemView.findViewById(R.id.ladakh_name)
        var description: TextView = itemView.findViewById(R.id.description)
        var tvCreated: TextView = itemView.findViewById(R.id.tvCreated)
        var tvPrice: TextView = itemView.findViewById(R.id.tvPrice)
        var tvCurreSign : TextView = itemView.findViewById(R.id.tvCurreSign)



    }




}