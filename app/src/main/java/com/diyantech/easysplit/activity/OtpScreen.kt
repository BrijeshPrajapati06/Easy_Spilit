package com.diyantech.easysplit.activity

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import com.diyantech.easysplit.ApiKtService.ApiInterface
import com.diyantech.easysplit.ApiKtService.ApiUtility
import com.diyantech.easysplit.R
import com.diyantech.easysplit.databinding.ActivityOtpScreenBinding
import com.diyantech.easysplit.modelclass.request.OtpRequest
import com.diyantech.easysplit.modelclass.request.ResendOtpRequest
import com.diyantech.easysplit.modelclass.request.SignupRequest
import com.diyantech.easysplit.modelclass.response.ForgotPasswordResponse
import com.diyantech.easysplit.modelclass.response.OtpResponse
import com.diyantech.easysplit.modelclass.response.ResendOtpResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OtpScreen : AppCompatActivity(R.layout.activity_otp_screen) {

    private lateinit var binding: ActivityOtpScreenBinding
    var otpResponse: OtpResponse? = null
    private var id: String? = null
    private var emailid: String? = null

    private val startTime = (15 * 1000).toLong()
    private val interval = (1 * 1000).toLong()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        startTimer()

        id = intent.getStringExtra("otp_id")

        emailid = intent.getStringExtra("emailId")
        binding.tvEmailId.text = emailid

        val window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = this.resources.getColor(R.color.white)

        binding.btnVerify.setOnClickListener {
            Toast.makeText(this, "click", Toast.LENGTH_SHORT).show()
            otpVerificationApi()
        }

        binding.pinView.requestFocus()
        var inputMethodManager: InputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.toggleSoftInput(
            InputMethodManager.SHOW_FORCED,
            InputMethodManager.HIDE_IMPLICIT_ONLY
        )



        binding.tvResendTime.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.txtTime.visibility = View.VISIBLE

            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        binding.tvResendTime.setOnClickListener {


            if (binding.txtTime.getVisibility() == View.GONE) {
                val hashMap = HashMap<String, Any>()
                hashMap.put("otp",binding.txtTime)
                otpResendApi()
            } else {
                binding.tvResendTime.isClickable = true
            }
        }

        binding.pinView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(
                charSequence: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                if (charSequence.toString().length == 4) {
                    Toast.makeText(applicationContext, "Working", Toast.LENGTH_SHORT).show()
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })




    }

    private fun otpVerificationApi() {

        var connected = false
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        connected = networkInfo != null && networkInfo.isConnected


        if (validOtp()) {
            var otpCode: String = binding.pinView.text.toString().trim { it <= ' ' }

            var otpRequest = OtpRequest(
                oTP = otpCode,
                userId = id ?: ""
            )

            if (connected) {
                Log.e(ContentValues.TAG, "onCreate: " + otpRequest)

                val userApi: ApiInterface =
                    ApiUtility.getUser().create(ApiInterface::class.java)

                val call: Call<OtpResponse> = userApi.sendOtpVerify(otpRequest)

                call.enqueue(object : Callback<OtpResponse> {

                    override fun onResponse(
                        call: Call<OtpResponse>,
                        response: Response<OtpResponse>,
                    ) {
                        if (response.isSuccessful) {
                            Log.e(
                                ContentValues.TAG,
                                "onResponse: " + (response.body()?.meta?.code ?: "Hello")
                            )
                            if (response.body()?.meta?.code == 200) {
//                                    savePrefsData()
//                                    saveUser()
                                otpResponse = response.body()!!
                                Toast.makeText(
                                    this@OtpScreen,
                                    "Successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
                                val intent =
                                    Intent(this@OtpScreen, CreateNewPassword::class.java)
                                intent.flags =
                                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                intent.putExtra("email_id", otpResponse?.data?.id)
                                startActivity(intent)
                            }
                        } else {

                            Toast.makeText(
                                this@OtpScreen,
                                "Otp Failed ",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<OtpResponse>, t: Throwable) {
                        Toast.makeText(
                            this@OtpScreen,
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

    private fun startTimer() {
        binding.txtTime?.setVisibility(View.VISIBLE)
        object : CountDownTimer(startTime, interval) {
            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                val currentTime = millisUntilFinished / 1000
                binding.txtTime?.setText("" + "0" + currentTime / 60 + " : " + if (currentTime % 60 >= 10) currentTime % 60 else "0" + currentTime % 60)
                //txt_time.setText("seconds remaining: " +String.valueOf(millisUntilFinished/1000));

//                txt_otp.setVisibility(View.GONE);
            }

            override fun onFinish() {
                binding.txtTime?.setVisibility(View.GONE)
                binding.tvResendTime?.setVisibility(View.VISIBLE)
            }

        }.start()
    }

    private fun otpResendApi() {

        var connected = false
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        connected = networkInfo != null && networkInfo.isConnected

        val resendOtpRequest = ResendOtpRequest(
            activity = 2,
            userId = id ?: ""
        )
        val apiInterface: ApiInterface =
            ApiUtility.getUser().create(ApiInterface::class.java)

        val call: Call<ResendOtpResponse> = apiInterface.reSendOtpVerify(resendOtpRequest)

        call.enqueue(object : Callback<ResendOtpResponse> {
            override fun onResponse(
                call: Call<ResendOtpResponse>,
                response: Response<ResendOtpResponse>
            ) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        if (response.body()!!.meta.code == 200) {
                            Toast.makeText(
                                this@OtpScreen,
                                "OTP Send Successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                            startTimer()
                        } else {
                            if (response.body()!!.meta.message != null) { Toast.makeText(this@OtpScreen, response.body()!!.meta.message, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                } else {
                    Toast.makeText(
                        this@OtpScreen,
                        "" + response.message(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<ResendOtpResponse>, t: Throwable) {

            }

        })

    }

    private fun validOtp(): Boolean {

//        var eOne = binding.et1.text.toString()
//        var eTwo = binding.et2.text.toString()
//        var eThree = binding.et3.text.toString()
//        var eFour = binding.et4.text.toString()
        var otp = binding.pinView.text.toString()


//
        if (otp.isEmpty()) {
            Toast.makeText(this@OtpScreen, "Please Enter Otp", Toast.LENGTH_SHORT).show()
        }
        return true
//    }
    }

//class GenericKeyEvent(private val currentView: EditText, private val previousView: EditText?) :
//    View.OnKeyListener {
//    override fun onKey(p0: View?, p1: Int, p2: KeyEvent?): Boolean {
//        if (p2!!.action == KeyEvent.ACTION_DOWN && p1 == KeyEvent.KEYCODE_DEL && currentView.id != R.id.et1 && currentView.text.isEmpty()) {
//            //If current is empty then previous EditText's number will also be deleted
//            previousView!!.text = null
//            previousView.requestFocus()
//            return true
//        }
//        return false
//    }
}