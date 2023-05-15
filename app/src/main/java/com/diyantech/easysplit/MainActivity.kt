package com.diyantech.easysplit

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.diyantech.easysplit.activity.AddUserActivity
import com.diyantech.easysplit.activity.SettingActivity
import com.diyantech.easysplit.adapter.DialogPurchaseAdapter
import com.diyantech.easysplit.adapter.TabViewPagerAdapter
import com.diyantech.easysplit.fragment.PurchaseFragment
import com.diyantech.easysplit.fragment.SplitFragment
import com.diyantech.easysplit.fragment.UserFragment
import com.diyantech.easysplit.modelclass.DialogPurchaseClass
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.textfield.TextInputLayout

class MainActivity : AppCompatActivity() {

    var tabLayout: TabLayout? = null
    var viewPager: ViewPager2? = null
    var ivSetting : ImageView? = null
    private lateinit var floatAddPlus: FloatingActionButton
    private lateinit var add_member: FloatingActionButton
    private lateinit var add_store: FloatingActionButton

    private var isAllFabsVisible: Boolean? = null

    // These are taken to make visible and invisible along with FABs
    private lateinit var tvAddMember: TextView
    private lateinit var tvNewPurchase: TextView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tabLayout = findViewById(R.id.tabLayout)
        viewPager = findViewById(R.id.viewPager)
        ivSetting = findViewById(R.id.ivSetting)

        floatAddPlus = findViewById(com.diyantech.easysplit.R.id.floatAddPlus)

        // FAB button
        add_member = findViewById(com.diyantech.easysplit.R.id.add_member)
        add_store = findViewById(com.diyantech.easysplit.R.id.add_store)

        // Also register the action name text, of all the FABs.
        tvAddMember = findViewById(com.diyantech.easysplit.R.id.tvAddMember)
        tvNewPurchase = findViewById(R.id.tvNewPurchase)

        Toast.makeText(this, "Hello World", Toast.LENGTH_SHORT).show()

        isAllFabsVisible = false
        add_member.visibility = View.GONE
        add_store.visibility = View.GONE
        tvAddMember.visibility = View.GONE
        tvNewPurchase.visibility = View.GONE

        viewPager?.isSaveEnabled = false

        val window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = this.resources.getColor(com.diyantech.easysplit.R.color.light_green)


        ivSetting?.setOnClickListener {
            var intent = Intent(this@MainActivity,SettingActivity::class.java)
            startActivity(intent)
            finish()
        }

        floatAddPlus.setOnClickListener(View.OnClickListener {
            (if (!isAllFabsVisible!!) {
                // when isAllFabsVisible becomes true make all
                // the action name texts and FABs VISIBLE
                add_member.show()
                add_member.size.dec()
                add_store.show()
                tvAddMember.visibility = View.VISIBLE
                tvNewPurchase.visibility = View.VISIBLE

                // make the boolean variable true as we
                // have set the sub FABs visibility to GONE
                true
            } else {
                // when isAllFabsVisible becomes true make
                // all the action name texts and FABs GONE.
                add_member.hide()
                add_store.hide()
                tvAddMember.visibility = View.GONE
                tvNewPurchase.visibility = View.GONE

                // make the boolean variable false as we
                // have set the sub FABs visibility to GONE
                false
            }).also { isAllFabsVisible = it }
        })

        add_store.setOnClickListener {
            val alert: android.app.AlertDialog.Builder =
                android.app.AlertDialog.Builder(this@MainActivity)
            val mView: View =
                layoutInflater.inflate(com.diyantech.easysplit.R.layout.new_purchase_dialog, null)
            val tl_event_name =
                mView.findViewById<View>(com.diyantech.easysplit.R.id.tl_event_name) as TextInputLayout
            val btn_save: Button =
                mView.findViewById<View>(com.diyantech.easysplit.R.id.btn_save) as Button
            val btn_cancel: Button =
                mView.findViewById<View>(com.diyantech.easysplit.R.id.btn_cancel) as Button

            var rv_dialog_purchase : RecyclerView = mView.findViewById<View>(R.id.rv_dialog_purchase) as RecyclerView
            var mDialogPurchaseDataList = arrayListOf<DialogPurchaseClass>()

            mDialogPurchaseDataList = ArrayList()

            mDialogPurchaseDataList.add(DialogPurchaseClass("All",false))
            mDialogPurchaseDataList.add(DialogPurchaseClass("Me",false))
            mDialogPurchaseDataList.add(DialogPurchaseClass("User-1",false))
            mDialogPurchaseDataList.add(DialogPurchaseClass("User-2",false))
            mDialogPurchaseDataList.add(DialogPurchaseClass("User-3",false))

//            for (i in mDialogPurchaseDataList.indices){
//                mDialogPurchaseDataList.add(DialogPurchaseClass(true))
//
//            }

            var adapter = DialogPurchaseAdapter(mDialogPurchaseDataList)
            rv_dialog_purchase?.adapter = adapter
            rv_dialog_purchase?.layoutManager = GridLayoutManager(this@MainActivity,3)

            alert.setView(mView)
            val alertDialog: android.app.AlertDialog? = alert.create()
            alertDialog?.setCanceledOnTouchOutside(false)
            btn_save.setOnClickListener(View.OnClickListener {
                alertDialog?.dismiss()
            })
            btn_cancel.setOnClickListener(View.OnClickListener {
//            myCustomMessage.setText(txt_inputText.text.toString())
                alertDialog?.dismiss()
            })
            alertDialog?.show()
        }

        // below is the sample action to handle add alarm FAB. Here it shows simple Toast msg
        // The Toast will be shown only when they are visible and only when user clicks on them
        add_member.setOnClickListener {
            var intent = Intent(this@MainActivity, AddUserActivity::class.java)
            startActivity(intent)
            finish()
        }


        setupViewPager()
        setupTabLayout()


    }


    @SuppressLint("NotifyDataSetChanged")
    private fun setupTabLayout() {
        val adapter = TabViewPagerAdapter(this@MainActivity, supportFragmentManager, 3, lifecycle)
        adapter.addFragment(UserFragment(), getString(R.string.User))
        adapter.addFragment(PurchaseFragment(), getString(R.string.Purchase))
        adapter.addFragment(SplitFragment(), getString(R.string.Split))
        adapter.notifyDataSetChanged()

        viewPager?.adapter = adapter
        TabLayoutMediator(tabLayout!!, viewPager!!) { tab, position ->
            tab.text = adapter.getPageTitle(position)
            viewPager?.setCurrentItem(tab.position, true)
        }.attach()
        //        val tabTitles = arrayOf("User", "Purchase", "Split")

        //        TabLayoutMediator(
        //            tabLayout!!, viewPager!!
        //        ) { tab, position -> tab.text = tabTitles[position]
        //            viewPager?.setCurrentItem(tab.position, true)}.attach()

    }

    private fun setupViewPager() {
        val adapter = TabViewPagerAdapter(
            this@MainActivity,
            supportFragmentManager,
            tabLayout!!.getTabCount(),
            lifecycle
        )
        viewPager?.adapter = adapter


    }

    override fun onBackPressed() {
        val viewPager = viewPager
        if (viewPager?.currentItem == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed()
        } else {
            // Otherwise, select the previous step.
            viewPager?.currentItem = viewPager?.currentItem?.minus(1)!!
        }
    }

}