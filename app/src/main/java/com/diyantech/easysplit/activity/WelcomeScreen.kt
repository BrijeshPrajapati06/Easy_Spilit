package com.diyantech.easysplit.activity

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button

import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.diyantech.easysplit.R
import com.diyantech.easysplit.adapter.ViewPagerAdapter
import com.diyantech.easysplit.databinding.ActivityWelcomeScreenBinding
import com.google.android.material.tabs.TabLayout

class WelcomeScreen : AppCompatActivity(R.layout.activity_welcome_screen) {

    private lateinit var binding: ActivityWelcomeScreenBinding

    var position = 0
    lateinit var imageList: List<Int>
    lateinit var textList : List<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = this.resources.getColor(R.color.dark_white)



        binding.btnGetStart.setOnClickListener {
            var intent = Intent(this@WelcomeScreen,LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        imageList = ArrayList()
        imageList = imageList + R.drawable.ig_welcome
        imageList = imageList + R.drawable.ig_billsplit


        textList = ArrayList()
        textList = textList + "Welcome to Easy Split"
//        textList = textList + "we help you and your friends splits bills fast and easyWelcome to Easy Split"
        textList = textList + "Bill splits evenly or seperatly"
//        textList = textList + "You and your friends can either split bills evenly amongst yourself or seperatly"

        val viewPagerAdapter = ViewPagerAdapter(this@WelcomeScreen, imageList,textList)
        binding.viewPager.adapter = viewPagerAdapter

        binding.tabHomeIndicator.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                position = tab!!.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                position = tab!!.position
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })
        binding.tabHomeIndicator.setupWithViewPager(binding.viewPager)
    }
}