package com.diyantech.easysplit.activity

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.os.Bundle
import android.text.Editable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.diyantech.easysplit.ApiKtService.ApiInterface
import com.diyantech.easysplit.ApiKtService.ApiUtility
import com.diyantech.easysplit.R
import com.diyantech.easysplit.databinding.ActivityLoginBinding
import com.diyantech.easysplit.modelclass.request.LoginRequest
import com.diyantech.easysplit.modelclass.response.LoginResponse
import com.diyantech.easysplit.utils.Constants
import com.diyantech.easysplit.utils.FieldValidators
import com.diyantech.easysplit.utils.Session
import com.diyantech.easysplit.utils.SharedPreference
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    var PREFS_KEY = "prefs"
    lateinit var shared: SharedPreferences
    var loginResponse: LoginResponse? = null

    private lateinit var sharedPref: SharedPreference



    @SuppressLint("MissingInflatedId", "SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()

        val window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = this.resources.getColor(R.color.dark_white)
        shared = getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE)

//        binding.etEmail?.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
//            }
//
//            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
//                binding.txtEnterEmail?.visibility = View.GONE
//            }
//
//            override fun afterTextChanged(s: Editable) {}
//        })
//
//        binding.etPswd?.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
//            }
//
//            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
//                binding.txtEnterPswd?.visibility = View.GONE
//            }
//
//            override fun afterTextChanged(s: Editable) {}
//        })

        val sharedPreference =  getSharedPreferences("APP_PREF",Context.MODE_PRIVATE)

        sharedPref = SharedPreference(applicationContext)


        var connected = false
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        connected = networkInfo != null && networkInfo.isConnected


        if (shared.getBoolean("isLogged", false)) {
            Toast.makeText(this@LoginActivity, "Success", Toast.LENGTH_SHORT).show()
            val intent = Intent(this@LoginActivity, HomeActivity::class.java)
            startActivity(intent)
            finish()
        } else {

            binding.btnLogIn.setOnClickListener {
                Toast.makeText(this@LoginActivity, "Success", Toast.LENGTH_SHORT).show()
                if (validateLoginEmail() && validateLoginPassword()) {

                    var username: String = binding.etEmail.text.toString().trim { it <= ' ' }
                    var pwd: String = binding.etPswd.text.toString().trim { it <= ' ' }

                    val loginRequest = LoginRequest(
                        email = username,
                        pwd = pwd
                    )

                    if (connected) {
                        Log.e(ContentValues.TAG, "onCreate: " + loginRequest)

                        val userApi: ApiInterface =
                            ApiUtility.getUser().create(ApiInterface::class.java)

                        val call: Call<LoginResponse> = userApi.sendData(loginRequest)

                        call.enqueue(object : Callback<LoginResponse> {

                            override fun onResponse(
                                call: Call<LoginResponse>,
                                response: Response<LoginResponse>,
                            ) {
                                if (response.isSuccessful) {
                                    Log.e(
                                        ContentValues.TAG,
                                        "onResponse: " + (response.body()?.meta?.code ?: "Hello")
                                    )
                                    if (response.body()?.meta?.code == 200) {
                                        savePrefsData()
                                        saveUser()
                                        loginResponse = response.body()!!
                                        Session.setCurrentUser(loginResponse!!.data)
                                        Toast.makeText(
                                            this@LoginActivity,
                                            "Successfully",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        val intent =
                                            Intent(this@LoginActivity, HomeActivity::class.java)
                                        intent.putExtra("emailId",binding.etEmail.text.toString())
                                        var editor = sharedPreference.edit()
                                        sharedPref.putString(Constants.KEY_ID, loginResponse!!.data.id)
                                        editor.putString("hostId",loginResponse?.data?.id)
                                        Log.d(TAG, "hostId: "+loginResponse?.data?.id)
                                        editor.putString("image",loginResponse?.data?.profileImage)
                                        Log.d(TAG, "image: "+loginResponse?.data?.profileImage)
                                        editor.putString("myToken",loginResponse?.data?.myToken)
                                        Log.d(TAG, "hostToken: "+loginResponse?.data?.myToken)
                                        editor.putString("email",loginResponse?.data?.email)
                                        editor.putString("fullName",loginResponse?.data?.fullName)
                                        Log.d(TAG, "onResponse: "+loginResponse?.data?.email)


                                        editor.apply()
                                        startActivity(intent)
                                        finish()

                                    }
                                } else {

                                    Toast.makeText(
                                        this@LoginActivity,
                                        "Login Failed ",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }

                            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                                Toast.makeText(
                                    this@LoginActivity,
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



        binding.tvForgotPswd.setOnClickListener {
            var intent = Intent(this@LoginActivity, ForgotPassword::class.java)
            startActivity(intent)
        }

        val SpanString = SpannableString(
            "don't have an account? Signup"
        )

        val signup: ClickableSpan = object : ClickableSpan() {
            override fun onClick(textView: View) {

                var intent = Intent(this@LoginActivity, SignUp::class.java)
                startActivity(intent)
                finish()
            }

        }

        SpanString.setSpan(signup, 23, 29, 0)

        binding.tvAnotherAccount.setMovementMethod(LinkMovementMethod.getInstance())
        binding.tvAnotherAccount.setText(SpanString, TextView.BufferType.SPANNABLE)
        binding.tvAnotherAccount.setSelected(true)
    }

    private fun setupListeners() {
        binding.etEmail.addTextChangedListener(TextFieldLoginValidation(binding.etEmail))
        binding.etPswd.addTextChangedListener(TextFieldLoginValidation(binding.etPswd))
    }

   inner class TextFieldLoginValidation(private val view: View) : TextWatcher {
       override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
       override fun afterTextChanged(s: Editable?) {
           binding.tlEmailId.error = null
           binding.tlEmailId.isErrorEnabled = false
           binding.tlPassword.error =null
           binding.tlPassword.isErrorEnabled = false
       }
       override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
           // checking ids of each text field and applying functions accordingly.
           when (view.id) {
               R.id.et_email -> {
                   validateLoginEmail()
               }
               R.id.et_pswd -> {
                   validateLoginPassword()
               }
           }
       }


   }


    private fun validateLoginPassword(): Boolean {
        if (binding.etPswd.text.toString().trim().isEmpty()) {
            binding.tlPassword.error = "Required Field!"
            binding.etPswd.requestFocus()
            return false
        } else if (binding.etPswd.text.toString().length < 6) {
            binding.tlPassword.error = "password can't be less than 6"
            binding.etPswd.requestFocus()
            return false
        } else if (!FieldValidators.isStringContainNumber(binding.etPswd.text.toString())) {
            binding.tlPassword.error = "Required at least 1 digit"
            binding.etPswd.requestFocus()
            return false
        } else if (!FieldValidators.isStringLowerAndUpperCase(binding.etPswd.text.toString())) {
            binding.tlPassword.error =
                "Password must contain upper and lower case letters"
            binding.etPswd.requestFocus()
            return false
        } else if (!FieldValidators.isStringContainSpecialCharacter(binding.etPswd.text.toString())) {
            binding.tlPassword.error = "1 special character required"
            binding.etPswd.requestFocus()
            return false
        } else {
            binding.tlPassword.isErrorEnabled = false
        }
        return true
    }

    private fun validateLoginEmail(): Boolean {
        if (binding.etEmail.text.toString   ().trim().isEmpty()) {
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

    private fun saveUser() {
        val editor = shared?.edit()
        editor?.putBoolean("isLogged", true)
        editor?.apply()
    }

    private fun savePrefsData() {
        val prefsEditor: SharedPreferences.Editor = shared!!.edit()
        val gson = Gson()
        val json = gson.toJson(loginResponse) // myObject - instance of MyObject

        prefsEditor.putString("MyObject", json)
        prefsEditor.apply()
        prefsEditor.commit()
    }
//    var isvalid = true
//    private fun validateLogin(): Boolean {
//        val email = binding.etEmail.text.toString()
//        var password = binding.etPswd.text.toString()
//
//        if (email.isEmpty()) {
//            binding.txtEnterEmail.visibility = View.VISIBLE
//            binding.etEmail.setBackgroundResource(R.drawable.error_bg)
//            isvalid = false
//        }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
//            Toast.makeText(this@LoginActivity, "enter valid email", Toast.LENGTH_SHORT).show()
//        }
//        if (password.isEmpty()) {
//            binding.txtEnterPswd.visibility = View.VISIBLE
//            binding.etPswd.setBackgroundResource(R.drawable.error_bg)
//            isvalid = false
//        } else if (password.length < 8) {
//            Toast.makeText(this@LoginActivity, "password should be  8 or more character", Toast.LENGTH_SHORT).show()
//        }
//        else {
////            var intent = Intent(this@SignUp, HomeActivity::class.java)
////            startActivity(intent)
////            finish()
//        }
////            val sp = getSharedPreferences("Login", Context.MODE_PRIVATE)
////
////            val ed = sp.edit()
////            ed.putString("id","br123@gmail.com")
////            ed.putString("password", "12345678")
////            ed.apply()
//
//
////        var sharedPreferences : SharedPreferences = getSharedPreferences("Login", MODE_PRIVATE)
////        val uName = sharedPreferences.getString("id", "br123@gmail.com")
////        val pswd = sharedPreferences.getString("password", "12345678")
//
////        if (email.equals(uName) && password.equals(pswd)){
////            var intent = Intent(this@LoginActivity, HomeActivity::class.java)
////            startActivity(intent)
////            finish()
////        }else{
////            Toast.makeText(this, "Please Enter Valid Email", Toast.LENGTH_SHORT).show()
////        }
//
//        return true
//
//    }


}