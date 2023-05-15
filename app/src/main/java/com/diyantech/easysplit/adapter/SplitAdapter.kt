package com.diyantech.easysplit.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.diyantech.easysplit.R
import com.diyantech.easysplit.modelclass.SplitClass

class SplitAdapter(private val mSplitList: ArrayList<SplitClass>) : RecyclerView.Adapter<SplitAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.itemsplit, parent, false)
        return SplitAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = mSplitList[position]

        holder.profile.setImageResource(model.igProfile)
        holder.profile2.setImageResource(model.igProfile2)
        holder.name?.text = model.name
        holder.right_arrow?.setImageResource(model.rightarrow)
        holder.price?.text = model.price
        holder.right_arrow2?.setImageResource(model.rightarrow2)
        holder.name2?.text = model.name2

    }

    override fun getItemCount(): Int {
          return mSplitList.size

    }
    class ViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView){
        var name : TextView = itemView.findViewById(R.id.name)
        var profile : ImageView = itemView.findViewById(R.id.profile)
        var profile2 : ImageView = itemView.findViewById(R.id.profile2)
        var right_arrow : ImageView = itemView.findViewById(R.id.right_arrow)
        var price : TextView = itemView.findViewById(R.id.price)
        var right_arrow2 : ImageView = itemView.findViewById(R.id.right_arrow2)
        var name2 : TextView = itemView.findViewById(R.id.name2)

    }
}