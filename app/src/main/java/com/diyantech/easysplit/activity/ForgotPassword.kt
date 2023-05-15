package com.diyantech.easysplit.activity

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.diyantech.easysplit.ApiKtService.ApiInterface
import com.diyantech.easysplit.ApiKtService.ApiUtility
import com.diyantech.easysplit.R
import com.diyantech.easysplit.databinding.ActivityForgotPasswordBinding
import com.diyantech.easysplit.databinding.ActivityLoginBinding
import com.diyantech.easysplit.modelclass.request.ForgotPasswordRequest
import com.diyantech.easysplit.modelclass.request.SignupRequest
import com.diyantech.easysplit.modelclass.response.ForgotPasswordResponse
import com.diyantech.easysplit.modelclass.response.SignupResponse
import com.diyantech.easysplit.utils.FieldValidators
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForgotPassword : AppCompatActivity(R.layout.activity_forgot_password) {

    private lateinit var binding: ActivityForgotPasswordBinding
    var forgotPasswordResponse : ForgotPasswordResponse? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()

        var connected = false
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        connected = networkInfo != null && networkInfo.isConnected

        binding.btnContinue.setOnClickListener {
            Toast.makeText(this@ForgotPassword, "Success", Toast.LENGTH_SHORT).show()

            if (validForgotPswd()){
                var emailId : String = binding.etEmail.text.toString().trim{ it <= ' '}

                val forgotPasswordRequest = ForgotPasswordRequest(
                    email = emailId,
                )

                if (connected) {
                    Log.e(ContentValues.TAG, "onCreate: " + forgotPasswordRequest)

                    val userApi: ApiInterface =
                        ApiUtility.getUser().create(ApiInterface::class.java)

                    val call: Call<ForgotPasswordResponse> = userApi.sendOtp(forgotPasswordRequest)

                    call.enqueue(object : Callback<ForgotPasswordResponse> {

                        override fun onResponse(
                            call: Call<ForgotPasswordResponse>,
                            response: Response<ForgotPasswordResponse>,
                        ) {
                            if (response.isSuccessful) {
                                Log.e(
                                    ContentValues.TAG,
                                    "onResponse: " + (response.body()?.meta?.code ?: "Hello")
                                )
                                if (response.body()?.meta?.code == 200) {
//                                    savePrefsData()
//                                    saveUser()
                                    forgotPasswordResponse = response.body()!!
                                    Toast.makeText(
                                        this@ForgotPassword,
                                        "Successfully",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    val intent =
                                        Intent(this@ForgotPassword, OtpScreen::class.java)
                                    intent.putExtra("otp_id", forgotPasswordResponse?.data?.id)
                                    intent.putExtra("emailId",binding.etEmail.text.toString())
                                    startActivity(intent)
                                }
                            } else {

                                Toast.makeText(
                                    this@ForgotPassword,
                                    "Login Failed ",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        override fun onFailure(call: Call<ForgotPasswordResponse>, t: Throwable) {
                            Toast.makeText(
                                this@ForgotPassword,
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
    }

    private fun setupListeners() {
        binding.etEmail.addTextChangedListener(TextFieldForgotPswdValidation(binding.etEmail))
    }

   inner class TextFieldForgotPswdValidation(private val view: View) : TextWatcher {
       override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

       override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
           when (view.id) {
               R.id.et_email -> {
                   validForgotPswd()
               }
           }
       }

       override fun afterTextChanged(s: Editable?) {
           binding.tlEmailId.error = null
           binding.tlEmailId.isErrorEnabled = false
       }

   }

    //    private fun validForgotPswd(): Boolean {
//        var email = binding.etEmail.text.toString()
//        if (email.isEmpty()) {
//            Toast.makeText(this@ForgotPassword, "Please Enter Email", Toast.LENGTH_SHORT).show()
//        }
//        return true
private fun validForgotPswd(): Boolean {
    if (binding.etEmail.text.toString().trim().isEmpty()) {
        binding.tlEmailId.error = "Required Field!"
        binding.etEmail.requestFocus()
        return false
    } else if (!FieldValidators.isValidEmail(binding.etEmail.text.toString())) {
        binding.tlEmailId.error = "Invalid Email!"
        binding.etEmail.requestFocus()
        return false
    } else {
        binding.tlEmailId.isErrorEnabled = false
    }
    return true
}

}