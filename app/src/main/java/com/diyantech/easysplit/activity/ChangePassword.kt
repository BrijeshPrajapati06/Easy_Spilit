package com.diyantech.easysplit.activity

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.diyantech.easysplit.ApiKtService.ApiInterface
import com.diyantech.easysplit.ApiKtService.ApiUtility
import com.diyantech.easysplit.R
import com.diyantech.easysplit.databinding.ActivityChangePasswordBinding
import com.diyantech.easysplit.databinding.ActivitySettingBinding
import com.diyantech.easysplit.modelclass.request.ChangePasswordRequest
import com.diyantech.easysplit.modelclass.request.CreatePassworsRequest
import com.diyantech.easysplit.modelclass.response.ChangePasswordResponse
import com.diyantech.easysplit.modelclass.response.CreatePasswordResponse
import com.diyantech.easysplit.modelclass.response.LoginResponse
import com.diyantech.easysplit.utils.FieldValidators
import com.diyantech.easysplit.utils.Session
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChangePassword : AppCompatActivity(R.layout.activity_change_password) {

    private lateinit var binding: ActivityChangePasswordBinding
    var changePasswordResponse : ChangePasswordResponse? = null
    private var id: String? = null
    var loginData: LoginResponse.Data? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()

        id = intent.getStringExtra("email_id")

        val window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = this.resources.getColor(com.diyantech.easysplit.R.color.light_green)

        if (Session.getCurrentUser() != null) {
            loginData = Session.getCurrentUser()

        }

        binding.btnSave.setOnClickListener {
            changePasswordApi()
        }

        binding.btnCancel.setOnClickListener {
            var intent = Intent(this@ChangePassword, SettingActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun setupListeners() {
        binding.etCurrentPassword.addTextChangedListener(TextFieldChangePswdValidation(binding.etCurrentPassword))
        binding.etNewPassword.addTextChangedListener(TextFieldChangePswdValidation(binding.etNewPassword))
        binding.etConformPassword.addTextChangedListener(TextFieldChangePswdValidation(binding.etConformPassword))
    }

   inner class TextFieldChangePswdValidation(private val view: View) : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            // checking ids of each text field and applying functions accordingly.
            when (view.id) {
                R.id.etCurrentPassword -> {
                    validCurrentPswd()
                }
                R.id.etNewPassword -> {
                   validNewPswd()
                }
                R.id.etConformPassword -> {
                   validConformPswd()
                }
            }
        }

        override fun afterTextChanged(s: Editable?) {
            binding.tlCurrentPswd.error = null
            binding.tlCurrentPswd.isErrorEnabled = false
            binding.tlNewPswd.error = null
            binding.tlNewPswd.isErrorEnabled = false
            binding.tlConformPsd.error = null
            binding.tlConformPsd.isErrorEnabled = false
        }

    }

    private fun changePasswordApi() {
        var connected = false
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        connected = networkInfo != null && networkInfo.isConnected

        if (validCurrentPswd() && validNewPswd() && validConformPswd()){
            var oldPswd : String = binding.etCurrentPassword.text.toString().trim{ it <= ' '}
            var newPswd : String = binding.etNewPassword.text.toString().trim{ it <= ' '}
            var conformPswd : String = binding.etConformPassword.text.toString().trim{ it <= ' '}

            val changePasswordRequest = ChangePasswordRequest(
                id = loginData?.id ?: "",
                newpwd = newPswd,
                oldpwd = oldPswd
            )
            Log.d(TAG, "onId: "+ loginData!!.id)
            if (connected) {
                Log.e(ContentValues.TAG, "onCreate: " + changePasswordRequest)

                val userApi: ApiInterface =
                    ApiUtility.getUser().create(ApiInterface::class.java)

                val call: Call<ChangePasswordResponse> = userApi.userChangePassword(
                    changePasswordRequest!!
                )

                call.enqueue(object : Callback<ChangePasswordResponse> {

                    override fun onResponse(
                        call: Call<ChangePasswordResponse>,
                        response: Response<ChangePasswordResponse>,
                    ) {
                        if (response.isSuccessful) {
                            Log.e(
                                ContentValues.TAG,
                                "onResponse: " + (response.body()?.meta?.code ?: "Hello")
                            )
                            if (response.body()?.meta?.code == 200) {
                                changePasswordResponse = response.body()!!
                                Toast.makeText(
                                    this@ChangePassword,
                                    "Successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
                                val intent =
                                    Intent(this@ChangePassword, LoginActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                        } else {

                            Toast.makeText(
                                this@ChangePassword,
                                "Login Failed ",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<ChangePasswordResponse>, t: Throwable) {
                        Toast.makeText(
                            this@ChangePassword,
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

    private fun validConformPswd(): Boolean {
        if (binding.etConformPassword.text.toString().trim().isEmpty()) {
            binding.tlConformPsd.error = "Required Field!"
            binding.etConformPassword.requestFocus()
            return false
        } else if (binding.etConformPassword.text.toString().length < 6) {
            binding.tlConformPsd.error = "password can't be less than 6"
            binding.etConformPassword.requestFocus()
            return false
        } else if (!FieldValidators.isStringContainNumber(binding.etConformPassword.text.toString())) {
            binding.tlConformPsd.error = "Required at least 1 digit"
            binding.etConformPassword.requestFocus()
            return false
        } else if (!FieldValidators.isStringLowerAndUpperCase(binding.etConformPassword.text.toString())) {
            binding.tlConformPsd.error =
                "Password must contain upper and lower case letters"
            binding.etConformPassword.requestFocus()
            return false
        } else if (!FieldValidators.isStringContainSpecialCharacter(binding.etConformPassword.text.toString())) {
            binding.tlConformPsd.error = "1 special character required"
            binding.etConformPassword.requestFocus()
            return false
        } else {
            binding.tlConformPsd.isErrorEnabled = false
        }
        return true
    }

    private fun validNewPswd(): Boolean {
        if (binding.etNewPassword.text.toString().trim().isEmpty()) {
            binding.tlNewPswd.error = "Required Field!"
            binding.etNewPassword.requestFocus()
            return false
        } else if (binding.etNewPassword.text.toString().length < 6) {
            binding.tlNewPswd.error = "password can't be less than 6"
            binding.etNewPassword.requestFocus()
            return false
        } else if (!FieldValidators.isStringContainNumber(binding.etNewPassword.text.toString())) {
            binding.tlNewPswd.error = "Required at least 1 digit"
            binding.etNewPassword.requestFocus()
            return false
        } else if (!FieldValidators.isStringLowerAndUpperCase(binding.etNewPassword.text.toString())) {
            binding.tlNewPswd.error =
                "Password must contain upper and lower case letters"
            binding.etNewPassword.requestFocus()
            return false
        } else if (!FieldValidators.isStringContainSpecialCharacter(binding.etNewPassword.text.toString())) {
            binding.tlNewPswd.error = "1 special character required"
            binding.etNewPassword.requestFocus()
            return false
        } else {
            binding.tlNewPswd.isErrorEnabled = false
        }
        return true
    }

    private fun validCurrentPswd(): Boolean {
        if (binding.etCurrentPassword.text.toString().trim().isEmpty()) {
            binding.tlCurrentPswd.error = "Required Field!"
            binding.etCurrentPassword.requestFocus()
            return false
        } else if (binding.etCurrentPassword.text.toString().length < 6) {
            binding.tlCurrentPswd.error = "password can't be less than 6"
            binding.etCurrentPassword.requestFocus()
            return false
        } else if (!FieldValidators.isStringContainNumber(binding.etCurrentPassword.text.toString())) {
            binding.tlCurrentPswd.error = "Required at least 1 digit"
            binding.etCurrentPassword.requestFocus()
            return false
        } else if (!FieldValidators.isStringLowerAndUpperCase(binding.etCurrentPassword.text.toString())) {
            binding.tlCurrentPswd.error =
                "Password must contain upper and lower case letters"
            binding.etCurrentPassword.requestFocus()
            return false
        } else if (!FieldValidators.isStringContainSpecialCharacter(binding.etCurrentPassword.text.toString())) {
            binding.tlCurrentPswd.error = "1 special character required"
            binding.etCurrentPassword.requestFocus()
            return false
        } else {
            binding.tlCurrentPswd.isErrorEnabled = false
        }
        return true
    }
}