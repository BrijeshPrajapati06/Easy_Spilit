package com.diyantech.easysplit.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.CompoundButton
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.diyantech.easysplit.R
import com.diyantech.easysplit.adapter.DialogPurchaseAdapter.ViewHolder
import com.diyantech.easysplit.modelclass.DialogPurchaseClass
import kotlinx.coroutines.handleCoroutineException

class DialogPurchaseAdapter(private val mDialogPurchaseList: ArrayList<DialogPurchaseClass>) : RecyclerView.Adapter<ViewHolder>() {

//    var selectedPosition = -1
//    var onItemClickListener : OnItemClickListener? = null
    private var isNewRadiButtonChecked = false
    private var lastCheckedPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.itemdialogpurchase, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = mDialogPurchaseList[position]
        holder.userOne.text = model.user

//        holder.rbUserone?.text = model.rbuser

//        holder.radio_group.setBackgroundResource()

        if (isNewRadiButtonChecked){
            holder.rbUserone.isChecked = model.rbuser
        } else {
            if (holder.adapterPosition == 0){
                holder.rbUserone.isChecked = true
                lastCheckedPosition = 0
            }
        }

    }

    override fun getItemCount(): Int {
        return mDialogPurchaseList.size
    }
   inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var rbUserone : RadioButton = itemView.findViewById(R.id.rbUserone)
       var userOne : TextView = itemView.findViewById(R.id.userOne)
//        var radio_group : RadioGroup = itemView.findViewById(R.id.radio_group)

        init {
            rbUserone.setOnClickListener{
                handleRadiobuttonChecks(adapterPosition)
            }
        }


    }
    private fun handleRadiobuttonChecks(adapterPosition: Int) {
        isNewRadiButtonChecked = true
        mDialogPurchaseList[lastCheckedPosition].rbuser = false
        mDialogPurchaseList[adapterPosition].rbuser = true
        lastCheckedPosition = adapterPosition
        notifyDataSetChanged()
    }

}