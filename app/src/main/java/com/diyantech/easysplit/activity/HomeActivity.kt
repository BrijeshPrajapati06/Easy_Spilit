package com.diyantech.easysplit.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.diyantech.easysplit.ApiKtService.*
import com.diyantech.easysplit.MainActivity
import com.diyantech.easysplit.R
import com.diyantech.easysplit.adapter.EventAdapter
import com.diyantech.easysplit.databinding.ActivityHomeBinding
import com.diyantech.easysplit.modelclass.request.EventCreateRequest
import com.diyantech.easysplit.modelclass.response.EventCreateResponse
import com.diyantech.easysplit.modelclass.response.GetEventPageDataResponse
import com.diyantech.easysplit.modelclass.response.LoginResponse
import com.diyantech.easysplit.utils.Constants
import com.diyantech.easysplit.utils.Session
import com.diyantech.easysplit.utils.SharedPreference
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeActivity() : AppCompatActivity(R.layout.activity_home), RequestListner,
    EventAdapter.OnItemClickListener {

    private lateinit var binding: ActivityHomeBinding
    private var pressedTime: Long = 0
    var eventCreateResponse: EventCreateResponse? = null
    var getEventDataResponse: GetEventPageDataResponse.Data.Result? = null
    var getEventId : GetEventPageDataResponse? = null
    var dataList: List<GetEventPageDataResponse.Data.Result>? = null
    var eventAdapter: EventAdapter? = null
    var loginResponse: LoginResponse? = null
    lateinit var linearLayoutManager: LinearLayoutManager
    var id: String? = null
    var strId: String? = null

    var pageNo = 0
    var lastPage: Int = 1

    //    var totalResults = -1
    private var loading = true
    private var isNextPage: Boolean = true
    var LastVisibleItems: Int? = null
    var totalItemCount: Int = 0
    var totalResults = -1
    var loginData: LoginResponse.Data? = null
    private lateinit var sharedPreference: SharedPreference

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)


        sharedPreference = SharedPreference(applicationContext)
//Create Layout Manager
        linearLayoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)


        binding.mRecycleView.layoutManager = linearLayoutManager

        if (Session.getCurrentUser() != null) {
            loginData = Session.getCurrentUser()
        }
        sharedPreference = SharedPreference(applicationContext)
        strId = sharedPreference.getString(Constants.KEY_ID)


        val sharedPreference = getSharedPreferences("APP_PREF", Context.MODE_PRIVATE)

        val hUserId = sharedPreference.getString("hostId", "Id")


        Log.d(TAG, "onIdUser: " + hUserId)


        getDataRvApi(hUserId, pageNo)



        binding.mRecycleView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {


                if (dy > 10) {
                    totalItemCount = linearLayoutManager!!.itemCount
                    LastVisibleItems = linearLayoutManager!!.findLastVisibleItemPosition()
                    if (!loading) {
                        if (totalItemCount - 1 == LastVisibleItems) {
                            if (isNextPage) {
                                Log.d("TAG", "isNextPage--->" + isNextPage)



                            }
                            loading = true
                            getDataRvApi(hUserId, pageNo)
                        }
                    }
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {

            }
        })

        binding.floatAddPlus.setOnClickListener {
            showEventDailog()
        }

        binding.ivSetting.setOnClickListener {
            var intent = Intent(this@HomeActivity, SettingActivity::class.java)
            startActivity(intent)
        }
    }


    private fun getDataRvApi(hUserId: String?, pageNo: Int) {
        Log.d(TAG, "RequestSent" + pageNo)
        val sharedPref =  getSharedPreferences("PREF",Context.MODE_PRIVATE)

        if (pageNo > lastPage) {
            binding.progressbar.setVisibility(View.VISIBLE)

//           getDataRvApi(hUserId, pageNo)
            lastPage = pageNo
        }

        binding.mRecycleView.layoutManager = LinearLayoutManager(this)

        val window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = this.resources.getColor(com.diyantech.easysplit.R.color.light_green)


        var connected = false
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        connected = networkInfo != null && networkInfo.isConnected


        val userApi: ApiInterface = ApiUtility.getUser().create(ApiInterface::class.java)
        val call: Call<GetEventPageDataResponse> = userApi.getEventData(hUserId!!, pageNo)


        call.enqueue(object : Callback<GetEventPageDataResponse> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<GetEventPageDataResponse>,
                response: Response<GetEventPageDataResponse>
            ) {

                if (response.isSuccessful && response.body() != null) {
                    dataList = response.body()!!.data.result
                    Log.d(TAG, "onSizeDataList: $dataList")

                    var getEventPageDataResponse: GetEventPageDataResponse? = response.body()
                    if (getEventDataResponse != null) {
                        binding.mRecycleView.setVisibility(View.GONE)
                        binding.igNoData.setVisibility(View.VISIBLE)
                        binding.txtNoData.setVisibility(View.VISIBLE)
                    } else {
                        binding.mRecycleView.setVisibility(View.VISIBLE)
                        binding.igNoData.setVisibility(View.GONE)
                        binding.txtNoData.setVisibility(View.GONE)

//                        totalResults = getEventPageDataResponse!!.data.recordsTotal


                        var gson = Gson()
                        var jsonString = gson.toJson(dataList)

                        Log.d("sTAG", "dataList: ---->" + jsonString)

                        var editor = sharedPref.edit()
                        editor.putString("eventId",getEventPageDataResponse!!.data.result.get(0).id)
                        Log.d(TAG, "eventId: "+getEventPageDataResponse!!.data.result.get(0).id)
                        editor.apply()

                        eventAdapter =
                            EventAdapter(
                                this@HomeActivity, this@HomeActivity,
                                getEventPageDataResponse!!.data.result as ArrayList<GetEventPageDataResponse.Data.Result>)
                        binding.mRecycleView?.adapter = eventAdapter
                        binding.mRecycleView?.layoutManager = LinearLayoutManager(this@HomeActivity)
                        eventAdapter?.notifyDataSetChanged()


                    }
                }
            }

            override fun onFailure(call: Call<GetEventPageDataResponse>, t: Throwable) {
                Toast.makeText(this@HomeActivity, "Error: " + t.message, Toast.LENGTH_SHORT)
                    .show()
            }
        })


    }

    var dialog: Dialog? = null

    private fun showEventDailog() {
        dialog = Dialog(this)

        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.setContentView(R.layout.custom_dialog)
        dialog?.setCancelable(true)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog?.window!!.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog?.window!!.attributes = lp
        dialog?.show()


        var btn_save = dialog?.findViewById<Button>(R.id.btn_save)
        btn_save?.setOnClickListener(View.OnClickListener {

            eventCreateApi()

            var intent = Intent(this@HomeActivity, HomeActivity::class.java)
            startActivity(intent)
            finish()

            dialog!!.dismiss()
        })


        var btn_cancel = dialog?.findViewById<Button>(R.id.btn_cancel)
        btn_cancel?.setOnClickListener(View.OnClickListener {
//            myCustomMessage.setText(txt_inputText.text.toString())
            dialog?.dismiss()
        })
    }

    private fun eventCreateApi() {

        var connected = false
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        connected = networkInfo != null && networkInfo.isConnected

        if (validCreateData()) {

            Log.d(TAG, "eventCreate: " + eventCreateResponse?.data?.hostId)

            var editName = dialog?.findViewById<TextInputEditText>(R.id.et_name_custom)
            var editDescription =
                dialog?.findViewById<TextInputEditText>(R.id.et_description_custom)

            var name: String = editName?.text.toString().trim { it <= ' ' }
            var description: String = editDescription?.text.toString().trim { it <= ' ' }


            var eventCreateRequest = EventCreateRequest(
                description = description,
                eventName = name
            )

            if (connected) {
                Log.e(TAG, "onCreate: " + eventCreateRequest)

                val userApi: ApiInterface =
                    ApiUtility.getUserToken().create(ApiInterface::class.java)

                val call: Call<EventCreateResponse> = userApi.eventCreate(eventCreateRequest)

                call.enqueue(object : Callback<EventCreateResponse> {

                    override fun onResponse(
                        call: Call<EventCreateResponse>,
                        response: Response<EventCreateResponse>,
                    ) {
                        if (response.isSuccessful) {
                            Log.e(
                                ContentValues.TAG,
                                "onResponse: " + (response.body()?.meta?.code ?: "Hello")
                            )
                            if (response.body()?.meta?.code == 200) {
                                eventCreateResponse = response.body()!!
                                Toast.makeText(
                                    this@HomeActivity,
                                    "Successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
//                                val intent =
//                                    Intent(this@HomeActivity, HomeActivity::class.java)
//                                intent.flags =
//                                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                                startActivity(intent)
//                                finish()
                            }
                        } else {

                            Toast.makeText(
                                this@HomeActivity,
                                "Event Failed ",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<EventCreateResponse>, t: Throwable) {
                        Toast.makeText(
                            this@HomeActivity,
                            "error: " + t.message,
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }

                })
            } else {
                Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun validCreateData(): Boolean {

        return true
    }


    override fun onItemClick(
        getEventDataResponse: GetEventPageDataResponse.Data.Result,
        position: Int
    ) {
        Toast.makeText(this, "selected", Toast.LENGTH_SHORT).show()
        var intent = Intent(this@HomeActivity, MainActivity::class.java)
        startActivity(intent)
    }

    override fun onComplete(requestCode: RequestCode?, `object`: Any?, message: String?) {
        when (requestCode) {
            FEED_POST_LIST -> {
                loading = false
                binding.progressbar.setVisibility(View.GONE)
                val getEventPageDataResponse: GetEventPageDataResponse? =
                    `object` as GetEventPageDataResponse?
                Log.d("TAG", "feed--->$getEventPageDataResponse")
                if (getEventPageDataResponse != null) {
                    if (getEventPageDataResponse.meta.code === 200) {
                        if (getEventPageDataResponse.data.nextPage !== 0) {
                            isNextPage = true
                            pageNo = getEventPageDataResponse.data.nextPage
                        } else {
                            isNextPage = false
                        }
                        if (getEventPageDataResponse.data.result.size > 0) {
                            dataList = getEventPageDataResponse.data.result
                            eventAdapter?.addList(dataList!!)
                            eventAdapter?.notifyDataSetChanged()
                        }
                    }
                }
            }
//            LIKE_DISLIKE_POST -> {
//                val likePostResponse: LikePostResponse? = `object` as LikePostResponse?
//                if (likePostResponse != null) {
//                    if (likePostResponse != null && likePostResponse.getMeta() != null) {
////                        feedAdapter.likeItem(like_pos);
//                    }
//                }
//            }
//            SAVE_UNSAVE_POST -> {
//                val saveUnsaveResponse: SaveUnsaveResponse? = `object` as SaveUnsaveResponse?
//                if (saveUnsaveResponse != null) {
//                    if (saveUnsaveResponse != null && saveUnsaveResponse.getMeta() != null) {
//                        feedAdapter.saveUnSaveItem(save_unSave_pos)
//                    }
//                }
//            }
            else -> {}
        }
    }

    override fun onException(error: String?, requestCode: RequestCode?) {
        TODO("Not yet implemented")
    }

    override fun onRetryRequest(requestCode: RequestCode?) {
        TODO("Not yet implemented")
    }

    override fun onRequestError(error: String?, status: Int, requestCode: RequestCode?) {
        TODO("Not yet implemented")
    }

}