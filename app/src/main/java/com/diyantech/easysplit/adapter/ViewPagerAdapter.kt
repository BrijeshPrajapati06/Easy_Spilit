package com.diyantech.easysplit.adapter

import android.content.Context
import android.graphics.PointF.length
import android.opengl.Matrix.length
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import com.diyantech.easysplit.R
import java.util.*


class ViewPagerAdapter(var context: Context,private var list: List<Int>,private var txtList : List<String>): PagerAdapter() {
    override fun getCount(): Int {
        return list.size
        return txtList.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as View
    }
    override fun instantiateItem(container: ViewGroup, position: Int): Any
    {
        val mLayoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val itemView: View = mLayoutInflater.inflate(R.layout.itemviewpager, container, false)

        val imageView: ImageView = itemView.findViewById<View>(R.id.images) as ImageView
        val tv_welcome_es: TextView = itemView.findViewById<View>(R.id.tv_welcome_es) as TextView
        val tv_bill_fasteasy: TextView = itemView.findViewById<View>(R.id.tv_bill_fasteasy) as TextView

        imageView.setImageResource(list.get(position))
        tv_welcome_es.setText(txtList.get(position))
        tv_bill_fasteasy.setText(txtList.get(position))

        Objects.requireNonNull(container).addView(itemView)


        return itemView
    }
    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any)
    {
        container.removeView(`object` as View)
    }

}