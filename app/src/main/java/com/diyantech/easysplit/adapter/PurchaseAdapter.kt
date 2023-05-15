package com.diyantech.easysplit.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.diyantech.easysplit.R
import com.diyantech.easysplit.modelclass.ModelAddUser
import com.diyantech.easysplit.modelclass.PurchaseClass

class PurchaseAdapter(private val mPurchaseList: ArrayList<PurchaseClass>) : RecyclerView.Adapter<PurchaseAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.itempurchase, parent, false)
        return PurchaseAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = mPurchaseList[position]

        holder.tv_hotel_room?.text = model.hotelRoom
        holder.tv_paid_user?.text = model.paidUser
        holder.tv_pay_everyone?.text = model.payEveryone
        holder.tv_price?.text = model.Price
        holder.number.setImageResource(model.igNum)

    }

    override fun getItemCount(): Int {
        return mPurchaseList.size
    }
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var number : ImageView = itemView.findViewById(R.id.number)
        var tv_hotel_room : TextView = itemView.findViewById(R.id.tv_hotel_room)
        var tv_paid_user : TextView = itemView.findViewById(R.id.tv_paid_user)
        var tv_pay_everyone : TextView = itemView.findViewById(R.id.tv_pay_everyone)
        var tv_price : TextView = itemView.findViewById(R.id.tv_price)
    }
}