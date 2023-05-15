package com.diyantech.easysplit.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.ContentValues
import android.content.Context
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.diyantech.easysplit.ApiKtService.*
import com.diyantech.easysplit.ApiKtService.ApiUtility.loginData
import com.diyantech.easysplit.R
import com.diyantech.easysplit.adapter.AddUserAdapter
import com.diyantech.easysplit.databinding.ActivityAddUserBinding
import com.diyantech.easysplit.modelclass.response.GetUserListResponse
import com.diyantech.easysplit.utils.Session
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddUserActivity : AppCompatActivity(R.layout.activity_add_user), RequestListner, AddUserAdapter.OnItemClickListener {

    private lateinit var binding: ActivityAddUserBinding

    var userList: List<GetUserListResponse.Data.Result>? = null
    var strId: String? = null
    var pageNo = 0
    var lastPage: Int = 1
    private var loading = true
    var getUserResponse: GetUserListResponse.Data.Result? = null
    private var isNextPage: Boolean = true
    var LastVisibleItems: Int? = null
    var totalItemCount: Int = 0
    var addUserAdapter: AddUserAdapter? = null
    lateinit var linearLayoutManager: LinearLayoutManager



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (Session.getCurrentUser() != null) {
            loginData = Session.getCurrentUser()
        }

        val sharedPreference = getSharedPreferences("PREF", Context.MODE_PRIVATE)

        val eventId = sharedPreference.getString("eventId", "Id")


        Log.d(ContentValues.TAG, "onIdEvent: " + eventId)
        //Create Layout Manager
        linearLayoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        getUserListDataApi(eventId,pageNo)

        binding.mRecycleView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {


                if (dy > 10) {
                    totalItemCount = linearLayoutManager!!.itemCount
                    LastVisibleItems = linearLayoutManager!!.findLastVisibleItemPosition()
                    if (!loading) {
                        if (totalItemCount - 1 == LastVisibleItems) {
                            if (isNextPage) {
                                Log.d("TAG", "isNextPage--->" + isNextPage)

                                getUserListDataApi(eventId, pageNo)

                            }
                            loading = true
                        }
                    }
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {

            }
        })


        binding.textAddUser.setOnClickListener {
          showAddUserDialog()
        }

    }

    private fun getUserListDataApi(eventId: String?, pageNo: Int) {
            Log.d(ContentValues.TAG, "RequestSent" + pageNo)
            if (pageNo > lastPage) {
//                binding.progressbar.setVisibility(View.VISIBLE)

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


            val userApi: ApiInterface = ApiUtility.getUserToken().create(ApiInterface::class.java)
            val call: Call<GetUserListResponse> = userApi.getUserData(eventId!!, pageNo)


            call.enqueue(object : Callback<GetUserListResponse> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(
                    call: Call<GetUserListResponse>,
                    response: Response<GetUserListResponse>
                ) {

                    if (response.isSuccessful && response.body() != null) {
                        userList = response.body()!!.data.result
                        Log.d(ContentValues.TAG, "onSizeData: ${userList!!.size}")


                        var getUserListResponse: GetUserListResponse? = response.body()
                        if (getUserResponse != null) {
                            binding.mRecycleView.setVisibility(View.GONE)
                        } else {
                            binding.mRecycleView.setVisibility(View.VISIBLE)

//                        totalResults = getEventPageDataResponse!!.data.recordsTotal


                            var gson = Gson()
                            var jsonString = gson.toJson(userList)

                            Log.d("sTAG", "userList: ---->" + jsonString)

                            addUserAdapter =
                                AddUserAdapter(
                                    this@AddUserActivity, this@AddUserActivity,
                                    getUserListResponse!!.data.result as ArrayList<GetUserListResponse.Data.Result>
                                )
                            binding.mRecycleView?.adapter = addUserAdapter
                            binding.mRecycleView?.layoutManager = LinearLayoutManager(this@AddUserActivity)
                            addUserAdapter?.notifyDataSetChanged()
                        }
                    }
                }

                override fun onFailure(call: Call<GetUserListResponse>, t: Throwable) {
                    Toast.makeText(this@AddUserActivity, "Error: " + t.message, Toast.LENGTH_SHORT)
                        .show()
                }
            })



    }

    var dialog: Dialog? = null
    private fun showAddUserDialog() {
        dialog = Dialog(this)

        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.setContentView(R.layout.item_user_add)
        dialog?.setCancelable(true)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog?.window!!.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog?.window!!.attributes = lp
        dialog?.show()


        var btn_add = dialog?.findViewById<Button>(R.id.btn_add)
        btn_add?.setOnClickListener(View.OnClickListener {

        })

        var btn_cancel = dialog?.findViewById<Button>(R.id.btn_cancel)
        btn_cancel?.setOnClickListener(View.OnClickListener {
            dialog?.dismiss()
        })
    }

    override fun onItemClick(getUserListResponse: GetUserListResponse.Data.Result, position: Int) {

    }

    override fun onComplete(requestCode: RequestCode?, `object`: Any?, message: String?) {
        when (requestCode) {
            FEED_POST_LIST -> {
                loading = false
//                binding.progressbar.setVisibility(View.GONE)
                val getUserListResponse: GetUserListResponse? =
                    `object` as GetUserListResponse?
                Log.d("TAG", "feed--->$getUserListResponse")
                if (getUserListResponse != null) {
                    if (getUserListResponse.meta.code === 200) {
                        if (getUserListResponse.data.result.size !== 0) {
                            isNextPage = true
                            pageNo = getUserListResponse.data.result.size
                        } else {
                            isNextPage = false
                        }
                        if (getUserListResponse.data.result.size > 0) {
                            userList = getUserListResponse.data.result
                            addUserAdapter?.addList(userList!!)
                            addUserAdapter?.notifyDataSetChanged()
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