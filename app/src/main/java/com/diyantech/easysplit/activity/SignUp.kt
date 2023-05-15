package com.diyantech.easysplit.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.bumptech.glide.Glide
import com.diyantech.easysplit.ApiKtService.ApiInterface
import com.diyantech.easysplit.ApiKtService.ApiUtility
import com.diyantech.easysplit.R
import com.diyantech.easysplit.databinding.ActivitySignUpBinding
import com.diyantech.easysplit.modelclass.request.SignupRequest
import com.diyantech.easysplit.modelclass.response.LoginResponse
import com.diyantech.easysplit.modelclass.response.SignupResponse
import com.diyantech.easysplit.utils.FieldValidators.isStringContainNumber
import com.diyantech.easysplit.utils.FieldValidators.isStringContainSpecialCharacter
import com.diyantech.easysplit.utils.FieldValidators.isStringLowerAndUpperCase
import com.diyantech.easysplit.utils.FieldValidators.isValidEmail
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.*


class SignUp : AppCompatActivity(R.layout.activity_sign_up) {

    private lateinit var binding: ActivitySignUpBinding
    var signupResponse: SignupResponse? = null
    var APP_FILE: File? = null
    var random = Random()
    var loginResponse: LoginResponse.Data? = null
    var Str_image: String? = null


    private lateinit var PERMISSIONS: Array<String>

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()

        val window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = this.resources.getColor(R.color.dark_white)

        val sharedPreference = getSharedPreferences("PREF", Context.MODE_PRIVATE)


        PERMISSIONS = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

        Str_image = signupResponse?.data?.profileImage

        val image: String = "http://122.170.0.3:3018/uploads/photos/" + Str_image
        Log.d(ContentValues.TAG, "onCreate: -->" + Str_image)

        Glide.with(this)
            .load(image)
            .error(R.drawable.ig_profile)
            .into(binding.ivProfile)

        dropDownTextView()

        binding.igEditProfile.setOnClickListener {
            if (!HasPermission(this@SignUp, PERMISSIONS)) {
                ActivityCompat.requestPermissions(this@SignUp, PERMISSIONS, 1)
            } else {
                selectImage()
            }
        }


        var connected = false
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        connected = networkInfo != null && networkInfo.isConnected

        binding.btnSignUp.setOnClickListener {
            Toast.makeText(this@SignUp, "Success", Toast.LENGTH_SHORT).show()

            if (validateUserName() && validateEmail() && validateMobileNo() && validateDropDown() && validatePassword() ) {
                var fullname: String = binding.etName.text.toString().trim { it <= ' ' }
                var username: String = binding.etEmail.text.toString().trim { it <= ' ' }
                var pwd: String = binding.etPswd.text.toString().trim { it <= ' ' }
                var number: String = binding.etNumber.text.toString().trim { it <= ' ' }

                val signupRequest = SignupRequest(
                    fullName = fullname,
                    profileImage = image,
                    email = username,
                    pwd = pwd,
                    mobile = number,
                    gender = "1"
                )

                if (connected) {
                    Log.e(ContentValues.TAG, "onCreate: " + signupRequest)

                    val userApi: ApiInterface =
                        ApiUtility.getUser().create(ApiInterface::class.java)

                    val call: Call<SignupResponse> = userApi.saveData(signupRequest)

                    call.enqueue(object : Callback<SignupResponse> {

                        override fun onResponse(
                            call: Call<SignupResponse>,
                            response: Response<SignupResponse>,
                        ) {
                            if (response.isSuccessful) {
                                Log.e(
                                    ContentValues.TAG,
                                    "onResponse: " + (response.body()?.meta?.code ?: "Hello")
                                )
                                if (response.body()?.meta?.code == 200) {
//                                    savePrefsData()
//                                    saveUser()
                                    signupResponse = response.body()!!
//                                    Session.setCurrentUser(signupResponse)
                                    Toast.makeText(
                                        this@SignUp,
                                        "Successfully",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    val intent =
                                        Intent(this@SignUp, LoginActivity::class.java)
                                    intent.flags =
                                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    var editor = sharedPreference.edit()
                                    editor.putString("number", signupResponse?.data?.mobile)
                                    Log.d(
                                        ContentValues.TAG,
                                        "hostNumber: " + signupResponse?.data?.mobile
                                    )
                                    editor.putString("image", signupResponse?.data?.profileImage)
                                    editor.apply()
                                    startActivity(intent)
                                    finish()
                                }
                            } else {

                                Toast.makeText(
                                    this@SignUp,
                                    "Login Failed ",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        override fun onFailure(call: Call<SignupResponse>, t: Throwable) {
                            Toast.makeText(
                                this@SignUp,
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

        val SpanString = SpannableString(
            "Already have a account? Login"
        )

        val login: ClickableSpan = object : ClickableSpan() {
            override fun onClick(textView: View) {
                var intent = Intent(this@SignUp, LoginActivity::class.java)
                startActivity(intent)
            }

        }
        SpanString.setSpan(login, 23, 29, 0)
//        SpanString.setSpan(login, 23, 29, 0)

        binding.tvLogin.setMovementMethod(LinkMovementMethod.getInstance())
        binding.tvLogin.setText(SpanString, TextView.BufferType.SPANNABLE)
        binding.tvLogin.setSelected(true)


    }

    private fun validateDropDown(): Boolean {
        val selectedView: View = binding.tlSelectGender
        if (selectedView != null && selectedView is TextView) {
            binding.tlSelectGender.requestFocus()
            val selectedTextView = selectedView as TextView
            selectedTextView.error = "error" // any name of the error will do
            selectedTextView.setTextColor(Color.RED) //text color in which you want your error message to be displayed
            selectedTextView.text = "error" // actual error message
            binding.tlSelectGender.performClick() // to open the spinner list if error is found.
        }
        return true
    }

    private fun dropDownTextView() {
        val item = arrayOf("Male","Female")

        var adapterItems: ArrayAdapter<String?>
        adapterItems = ArrayAdapter(this, R.layout.dropdown_item, item)
        binding.autoCompleteTxtView.setAdapter(adapterItems)

        binding.autoCompleteTxtView.setOnItemClickListener(object : OnItemClickListener{
            override fun onItemClick(
                adapterView: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val item1: String = adapterView?.getItemAtPosition(position).toString()
                Toast.makeText(this@SignUp, "item "+ item1, Toast.LENGTH_SHORT).show()
            }

        })

    }

    private fun setupListeners() {
        binding.etName.addTextChangedListener(TextFieldValidation(binding.etName))
        binding.etEmail.addTextChangedListener(TextFieldValidation(binding.etEmail))
        binding.etPswd.addTextChangedListener(TextFieldValidation(binding.etPswd))
        binding.etNumber.addTextChangedListener(TextFieldValidation(binding.etNumber))
    }

    inner class TextFieldValidation(private val view: View) : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun afterTextChanged(s: Editable?) {
            binding.tlFullName.error = null
            binding.tlFullName.isErrorEnabled = false
            binding.tlEmailId.error = null
            binding.tlEmailId.isErrorEnabled = false
            binding.tlPassword.error = null
            binding.tlPassword.isErrorEnabled = false
            binding.tlMobileNumber.error = null
            binding.tlMobileNumber.isErrorEnabled = false

        }
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            // checking ids of each text field and applying functions accordingly.
            when (view.id) {
                R.id.et_name -> {
                    validateUserName()
                }
                R.id.et_email -> {
                    validateEmail()
                }
                R.id.et_pswd -> {
                    validatePassword()
                }
                R.id.et_number -> {
                    validateMobileNo()
                }
                R.id.auto_complete_txtView -> {
                    validateDropDown()
                }
            }
        }
    }


    private fun validateMobileNo(): Boolean {
        val phoneNumber = binding.etNumber.text.toString()
        if (!phoneNumber.matches(".*[0-9].*".toRegex())) {
            binding.tlMobileNumber.error = "Please Enter Mobile Number"
            binding.etNumber.requestFocus()
            return false
        } else if (phoneNumber.length < 10) {
            binding.tlMobileNumber.error = "Please Enter 10 Digits"
            binding.etNumber.requestFocus()
            return false
        }
        return true
    }

    private fun validatePassword(): Boolean {
        if (binding.etPswd.text.toString().trim().isEmpty()) {
            binding.tlPassword.error = "Please Enter Password"
            binding.etPswd.requestFocus()
            return false
        } else if (binding.etPswd.text.toString().length < 6) {
            binding.tlPassword.error = "password can't be less than 6"
            binding.etPswd.requestFocus()
            return false
        } else if (!isStringContainNumber(binding.etPswd.text.toString())) {
            binding.tlPassword.error = "Required at least 1 digit"
            binding.etPswd.requestFocus()
            return false
        } else if (!isStringLowerAndUpperCase(binding.etPswd.text.toString())) {
            binding.tlPassword.error =
                "Password must contain upper and lower case letters"
            binding.etPswd.requestFocus()
            return false
        } else if (!isStringContainSpecialCharacter(binding.etPswd.text.toString())) {
            binding.tlPassword.error = "1 special character required"
            binding.etPswd.requestFocus()
            return false
        } else {
            binding.tlPassword.isErrorEnabled = false
        }
        return true
    }

    private fun validateEmail(): Boolean {
        if (binding.etEmail.text.toString().trim().isEmpty()) {
            binding.tlEmailId.error = "Please Enter Email"
            binding.etEmail.requestFocus()
            return false
        } else if (!isValidEmail(binding.etEmail.text.toString())) {
            binding.tlEmailId.error = "Invalid Email!"
            binding.etEmail.requestFocus()
            return false
        } else {
            binding.tlEmailId.isErrorEnabled = false
        }
        return true
    }

    private fun validateUserName(): Boolean {
        if (binding.etName.text.toString().trim().isEmpty()) {
            binding.tlFullName.error = "Please Enter UserName"
            binding.etName.requestFocus()
            return false
        } else {
            binding.tlFullName.isErrorEnabled = false
        }
        return true
    }

    private fun selectImage() {
        val dialog = Dialog(this@SignUp)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.editprofiledialog)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val ig_camera = dialog.findViewById<ImageView>(R.id.ig_camera)
        val ig_gallery = dialog.findViewById<ImageView>(R.id.ig_gallery)
        var txt_cancel = dialog.findViewById<TextView>(R.id.txt_cancel)

        txt_cancel.setOnClickListener {
            dialog.dismiss()
        }

        ig_camera.setOnClickListener {
            val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(takePicture, 0)
            dialog.dismiss()
        }

        ig_gallery.setOnClickListener {
            val pickPhoto = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )
            startActivityForResult(pickPhoto, 1)
            dialog.dismiss()
        }

        dialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            0 -> if (resultCode == RESULT_OK) {
                val extras = data?.extras
                val imageBitmap = extras!!["data"] as Bitmap?
                val tempUri: Uri = getImageUri(applicationContext, imageBitmap)
                val finalFile: File = File(getRealPathFromURI(tempUri))
                Log.e("TAG", "camera----->${finalFile.path}")
                APP_FILE = finalFile

                Log.e("TAG", "app_file----->" + APP_FILE!!.path)
                Glide.with(this)
                    .load(finalFile.path)
                    .error(R.drawable.ig_profile)
                    .into(binding.ivProfile)
            }
            1 -> if (resultCode == RESULT_OK) {
                val selectedImage = data?.data
                val finalFile: File = File(selectedImage?.let { getRealPathFromURI(it) })
                Log.e("TAG", "gallery----->$finalFile")
                APP_FILE = finalFile
                Glide.with(this)
                    .load(finalFile.path)
                    .error(R.drawable.ig_profile)
                    .into(binding.ivProfile)
            }
        }
    }

    private fun getRealPathFromURI(tempUri: Uri): String {
        var path: String = ""
        if (contentResolver != null) {
            val cursor = contentResolver.query(tempUri, null, null, null, null)
            if (cursor != null) {
                cursor.moveToFirst()
                val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
                path = cursor.getString(idx)
                cursor.close()
            }
        }
        return path
    }

    private fun getImageUri(applicationContext: Context?, imageBitmap: Bitmap?): Uri {
        val id = String.format("%04d", random.nextInt(10000))
        val bytes = ByteArrayOutputStream()
        imageBitmap!!.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(
            applicationContext!!.contentResolver,
            imageBitmap,
            id,
            null
        )
        return Uri.parse(path)
    }


    private fun HasPermission(signUp: SignUp, permissions: Array<String>?): Boolean {
        if (signUp != null && permissions != null) {
            for (permission in permissions) {
                if (ActivityCompat.checkSelfPermission(
                        signUp,
                        permission
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return false
                }
            }
        }
        return true
    }
}

