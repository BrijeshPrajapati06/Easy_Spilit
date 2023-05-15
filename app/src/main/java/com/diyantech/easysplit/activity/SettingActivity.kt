package com.diyantech.easysplit.activity

import android.app.Dialog
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.diyantech.easysplit.ApiKtService.ApiInterface
import com.diyantech.easysplit.ApiKtService.ApiUtility
import com.diyantech.easysplit.R
import com.diyantech.easysplit.adapter.CurrencyDropAdapter
import com.diyantech.easysplit.adapter.EventAdapter
import com.diyantech.easysplit.databinding.ActivitySettingBinding
import com.diyantech.easysplit.modelclass.response.CurrencyDropListResponse
import com.diyantech.easysplit.utils.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SettingActivity : AppCompatActivity(R.layout.activity_setting),
    CurrencyDropAdapter.OnItemClickListener {

    private lateinit var binding: ActivitySettingBinding
    var str_image: String? = null

    var selected: Boolean = false
    var mList: List<CurrencyDropListResponse.Data>? = null
    var linearLayoutManager: LinearLayoutManager? = null
    var currencyDropAdapter: CurrencyDropAdapter? = null
    var recycleview: RecyclerView? = null
    private var currency = ""
    private var currency1 = ""
    var arrayCountryList = ArrayList<String>()
    var arrayCountryList1 = ArrayList<String>()
    var pos = 0
    var currencyDropList: CurrencyDropListResponse? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = this.resources.getColor(com.diyantech.easysplit.R.color.light_green)


        val image: String = "http://122.170.0.3:3018/uploads/photos/" + str_image
        Log.d(TAG, "onCreate: -->$image")

        Glide.with(this)
            .load(image)
            .error(R.drawable.ig_profile)
            .into(binding.imgcivprofile)

        val sharedPreference = getSharedPreferences("APP_PREF", Context.MODE_PRIVATE)

        val hUseremailId = sharedPreference.getString("email", "email")
        val hUserFullName = sharedPreference.getString("fullName", "Brijesh")


        Log.d(TAG, "onIdUser: " + hUseremailId)

        binding.emailId.text = hUseremailId
        binding.uName.text = hUserFullName

        binding.logOut.setOnClickListener {
            showLogoutDialog()

        }

        binding.txtMyProfile.setOnClickListener {
            var intent = Intent(this@SettingActivity, EditProfileActivity::class.java)
            startActivity(intent)
        }

        binding.imgcivprofile.setOnClickListener {
            var intent = Intent(this@SettingActivity, EditProfileActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.txtCurrency.setOnClickListener {
            ShowCurrencyDialog()
            getCurrencyDropApi()
        }


        binding.txtChangePswd.setOnClickListener {
            var intent = Intent(this@SettingActivity, ChangePassword::class.java)
            startActivity(intent)
            finish()
        }

    }

    private fun showLogoutDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.logout_dialog)
        dialog.setCancelable(true)

        var cancel = dialog.findViewById<Button>(R.id.cancel)
        var logOut = dialog.findViewById<Button>(R.id.btn_logout)

        logOut.setOnClickListener {
            val pref = getSharedPreferences("prefs", Context.MODE_PRIVATE)
            val editor = pref.edit()
            pref.edit().remove("isLogged")
            editor.clear()
            editor.commit()
            var intent = Intent(this@SettingActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        cancel.setOnClickListener {
            dialog.dismiss()
        }

        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window!!.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog.window!!.attributes = lp
        dialog.show()
    }


    fun ShowCurrencyDialog() {
        val selected = 0 // or whatever you want

        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.currency_dialog)

        recycleview = dialog.findViewById(R.id.recycleView)

        recycleview?.layoutManager = linearLayoutManager
        recycleview?.adapter = currencyDropAdapter

        dialog.setCancelable(true)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window!!.attributes)
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog.window!!.attributes = lp
        dialog.show()


    }

    private fun getCurrencyDropApi() {

        if (Utils.checkNetwork(applicationContext)) {


            val apiInterface: ApiInterface =
                ApiUtility.getUser().create(ApiInterface::class.java)

            val call: Call<CurrencyDropListResponse> = apiInterface.getCurrencyDropListData()

            call?.enqueue(object : Callback<CurrencyDropListResponse> {
                override fun onResponse(
                    call: Call<CurrencyDropListResponse>,
                    response: Response<CurrencyDropListResponse>
                ) {
                    Toast.makeText(this@SettingActivity, "Api call  ", Toast.LENGTH_SHORT)
                        .show()
                    var currencyDropListResponse: CurrencyDropListResponse? = response.body()
                    var position: Int? = null
//                    if (currencyDropListResponse != null){
//                        Log.d(TAG, "onResponse: "+ currencyDropListResponse.toString())
//                        currencyDropAdapter = CurrencyDropAdapter(this@SettingActivity,currencyDropListResponse.data,this@SettingActivity)
//                        recycleview?.adapter = currencyDropAdapter
//                        recycleview?.layoutManager = LinearLayoutManager(this@SettingActivity)
//                    }
                    if (response.isSuccessful && response.body()?.data != null) {
                        mList = response.body()!!.data
                        Log.d(TAG, "onResponse: " + mList!!.size)
                        CreateCurrencyListArray(mList!!)

                    }


/*if (response.isSuccessful && response.body() != null){

                    mList?.addAll(response.body()!!.data)
                     Log.d(TAG, "onResponse: "+mList?.size)

                     var gson = Gson()
                     var jsonString = gson.toJson(mList)

                     Log.d("sTAG", "mList: ---->" + jsonString)

                     linearLayoutManager = LinearLayoutManager(this@SettingActivity)
                     recycleview?.adapter = currencyDropAdapter
                     currencyDropAdapter?.notifyDataSetChanged()


                 }*/

                }

                override fun onFailure(call: Call<CurrencyDropListResponse>, t: Throwable) {

                }
            })
        } else {
            Toast.makeText(this, "please check your internet connection ", Toast.LENGTH_SHORT)
                .show()
        }

    }

    private fun CreateCurrencyListArray(mList: List<CurrencyDropListResponse.Data>) {
        Log.e(TAG, "dataList:--->" + mList)


        for (i in mList.indices) {
            arrayCountryList.add(mList.get(i).currencyName)
            arrayCountryList1.add(mList.get(i).currencySign)
            Log.d(TAG, "CreateCurrencyListArray: " + arrayCountryList)
            if (mList.get(i).currencyName.equals("Currency") && mList.get(i).currencySign.equals("₹")
            ) {
                pos = i
            }
        }
        currencyDropAdapter = CurrencyDropAdapter(this@SettingActivity, mList, this@SettingActivity)
        recycleview?.adapter = currencyDropAdapter
        recycleview?.layoutManager = LinearLayoutManager(this@SettingActivity)


    }

    override fun onItemClick(
        currencyDropListResponse: CurrencyDropListResponse.Data,
        position: Int
    ) {
        var dialog = Dialog(this@SettingActivity)
        Toast.makeText(this, "Selected", Toast.LENGTH_SHORT).show()
        currency = currencyDropListResponse.toString()

        if (mList != null && mList!!.size > 0) {
            currency = mList!!.get(position).currencyName
            currency1 = mList!!.get(position).currencySign
            binding.txtCurrency.text = currency
            binding.txtCurrencySign.text = currency1
            Log.d("tag", "position-->$mList")
            val preference = getSharedPreferences("PREF", Context.MODE_PRIVATE)
            var editor = preference.edit()
            editor.putString("currencySign", currency1)
            Log.d(TAG, "onResponseSign: " + currency1)
            editor.apply()
            editor.commit()
            var intent = Intent(this@SettingActivity, HomeActivity::class.java)
            startActivity(intent)
            finish()
            dialog.dismiss()
        } else {
            Toast.makeText(this@SettingActivity, "failed", Toast.LENGTH_SHORT).show()
        }
//        currency = mList?.get(position)!!.currencySign
//        binding.txtCurrency.text = currency

//        Log.d("tag", "position-->$currency")
    }
}