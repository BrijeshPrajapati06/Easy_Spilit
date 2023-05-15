package com.diyantech.easysplit.activity

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.diyantech.easysplit.ApiKtService.ApiInterface
import com.diyantech.easysplit.ApiKtService.ApiUtility
import com.diyantech.easysplit.R
import com.diyantech.easysplit.databinding.ActivityCreateNewPasswordBinding
import com.diyantech.easysplit.databinding.ActivityForgotPasswordBinding
import com.diyantech.easysplit.modelclass.request.CreatePassworsRequest
import com.diyantech.easysplit.modelclass.request.ForgotPasswordRequest
import com.diyantech.easysplit.modelclass.response.CreatePasswordResponse
import com.diyantech.easysplit.modelclass.response.ForgotPasswordResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateNewPassword : AppCompatActivity() {

    lateinit var binding: ActivityCreateNewPasswordBinding
    private var id: String? = null
    var createPasswordResponse : CreatePasswordResponse? = null
    var forgotPasswordResponse : ForgotPasswordResponse? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateNewPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        id = intent.getStringExtra("email_id")

        binding.btnContinue.setOnClickListener {
            resetPasswordApi()
        }

    }

    private fun resetPasswordApi() {
        var connected = false
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        connected = networkInfo != null && networkInfo.isConnected

        if (validResetPswd()){
            var editPswd : String = binding.txteditPassword.text.toString().trim{ it <= ' '}

            val createPassworsRequest = CreatePassworsRequest(
                pwd = editPswd,
                userId = id ?: ""
            )

            if (connected) {
                Log.e(ContentValues.TAG, "onCreate: " + createPassworsRequest)

                val userApi: ApiInterface =
                    ApiUtility.getUser().create(ApiInterface::class.java)

                val call: Call<CreatePasswordResponse> = userApi.userResetPassword(createPassworsRequest)

                call.enqueue(object : Callback<CreatePasswordResponse> {

                    override fun onResponse(
                        call: Call<CreatePasswordResponse>,
                        response: Response<CreatePasswordResponse>,
                    ) {
                        if (response.isSuccessful) {
                            Log.e(
                                ContentValues.TAG,
                                "onResponse: " + (response.body()?.meta?.code ?: "Hello")
                            )
                            if (response.body()?.meta?.code == 200) {
                                createPasswordResponse = response.body()!!
                                Toast.makeText(
                                    this@CreateNewPassword,
                                    "Successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
                                val intent =
                                    Intent(this@CreateNewPassword, LoginActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                        } else {

                            Toast.makeText(
                                this@CreateNewPassword,
                                "Login Failed ",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<CreatePasswordResponse>, t: Throwable) {
                        Toast.makeText(
                            this@CreateNewPassword,
                            "error: " + t.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
            } else {
                Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun validResetPswd(): Boolean {
        var pswd = binding.txteditPassword.text.toString()

        if (pswd.isEmpty()) {
            Toast.makeText(this@CreateNewPassword, "Enter New Password", Toast.LENGTH_SHORT).show()
        }
        return true

    }
}