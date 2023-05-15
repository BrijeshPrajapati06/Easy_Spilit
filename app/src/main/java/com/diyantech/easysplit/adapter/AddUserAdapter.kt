package com.diyantech.easysplit.adapter

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.diyantech.easysplit.ApiKtService.ApiUtility
import com.diyantech.easysplit.ApiKtService.ApiUtility.loginData
import com.diyantech.easysplit.R
import com.diyantech.easysplit.modelclass.EventClass
import com.diyantech.easysplit.modelclass.ModelAddUser
import com.diyantech.easysplit.modelclass.response.GetEventPageDataResponse
import com.diyantech.easysplit.modelclass.response.GetUserListResponse
import com.diyantech.easysplit.utils.Session

class AddUserAdapter(var context: Context? = null, var onItemClickListener: AddUserAdapter.OnItemClickListener
,private val mList: ArrayList<GetUserListResponse.Data.Result>) :
    RecyclerView.Adapter<AddUserAdapter.ViewHolder>() {

    var str_image: String? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.itemadduser, parent, false)
        return AddUserAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = mList[position]

        if (Session.getCurrentUser() != null){
            loginData = Session.getCurrentUser()
        }

        str_image = loginData?.profileImage

        val image: String = "http://122.170.0.3:3018/uploads/photos/" + model.profileImage
        Log.d(TAG, "onBindViewHolder: "+ image)
//        val sharedPreference = context?.getSharedPreferences("PREF", Context.MODE_PRIVATE)
//
//        val img = sharedPreference?.getString("image", "ig")


//        Log.d(TAG, "onImg: " + img)

        holder.userName?.text = model.fullName
//        try{
    //            holder.userProfile.setImageResource(model.profileImage.toInt())
//        } catch(e : Exception){ // handle your exception
//
//        }

        Glide.with(context!!)
            .load(image)
            .error(R.drawable.ig_profile)
            .into(holder.userProfile)

    }

    override fun getItemCount(): Int {
        return mList.size
    }

    fun addList(userList: List<GetUserListResponse.Data.Result>) {
        if (userList != null){
            mList.addAll(userList)
            notifyDataSetChanged()
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var userProfile: ImageView = itemView.findViewById(R.id.userProfile)
        var userName: TextView = itemView.findViewById(R.id.userName)
    }

    interface OnItemClickListener {
        fun onItemClick(getUserListResponse: GetUserListResponse.Data.Result, position: Int)
    }
}