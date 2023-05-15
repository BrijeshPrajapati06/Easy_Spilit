package com.diyantech.easysplit.activity

import android.Manifest
import android.app.AlertDialog
import android.app.Dialog
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.view.isEmpty
import com.bumptech.glide.Glide
import com.diyantech.easysplit.ApiKtService.ApiInterface
import com.diyantech.easysplit.ApiKtService.ApiUtility
import com.diyantech.easysplit.BuildConfig
import com.diyantech.easysplit.R
import com.diyantech.easysplit.databinding.ActivityEditProfileBinding
import com.diyantech.easysplit.modelclass.response.LoginResponse
import com.diyantech.easysplit.modelclass.response.SignupResponse
import com.diyantech.easysplit.modelclass.response.UserUpdateResponse
import com.diyantech.easysplit.utils.Constants
import com.diyantech.easysplit.utils.Session
import com.diyantech.easysplit.utils.SharedPreference
import com.diyantech.easysplit.utils.Utils
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.*

class EditProfileActivity : AppCompatActivity(R.layout.activity_edit_profile) {

    private lateinit var binding: ActivityEditProfileBinding
    var APP_FILE: File? = null
    var mPart: MultipartBody.Part? = null
    var str_id: String? = null
    var Str_image: String? = null
    var Str_name: String? = null
    var Str_number: String? = null
    var Str_email: String? = null
    var isGender: Boolean? = false
    var strImg : String? = null
    var random = Random()
    var signupResponse: SignupResponse.Data? = null
    var loginResponse: LoginResponse.Data? = null
    private lateinit var PERMISSIONS: Array<String>
    private lateinit var sharedPreference: SharedPreference




    var userUpdateResponse: UserUpdateResponse? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = this.resources.getColor(com.diyantech.easysplit.R.color.light_green)

        sharedPreference = SharedPreference(applicationContext)

        PERMISSIONS = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

        if (Session.getCurrentUser() != null){
            loginResponse = Session.getCurrentUser()

            Str_image = loginResponse!!.profileImage
            Log.d(TAG, "onCreate: "+Str_image)
        }
//        str_id = intent.getStringExtra("otp_id")

//                val sharedPreference = getSharedPreferences("PREF", Context.MODE_PRIVATE)
//
//        val hUserNumber = sharedPreference.getString("number", "MobileNumber")
//        Log.d(TAG, "onIdUser: " + hUserNumber)


//        if (Session.getCurrentUser() != null) {
//            loginResponse = Session.getCurrentUser()
//
//            str_id = loginResponse?.id
//            Log.d(TAG, "onId: -->" + str_id)
//            Str_image = loginResponse?.profileImage
//            Str_name = loginResponse?.fullName
//            Str_email = loginResponse?.email
//            Str_number = signupResponse?.mobile
//            isGender = true
//        }
        val sharedPref = getSharedPreferences("APP_PREF", Context.MODE_PRIVATE)

        val profileImg = sharedPref.getString("image", "ig")


        Log.d(TAG, "onIgUser: " + profileImg)

        binding.etName.setText(Str_name)
        binding.etEmail.setText(Str_email)
        binding.etNumber.setText(Str_number)

        val image: String = "http://122.170.0.3:3018/uploads/photos/" + Str_image
        Log.d(TAG, "onCreateImg: -->$image")

        Glide.with(this)
            .load(image)
            .error(R.drawable.ig_profile)
            .into(binding.imgCivProfile)



        binding.btnSave.setOnClickListener {
            sharedPreference.putString(Constants.KEY_FULL_NAME, binding.etName?.text.toString())
            Log.d(TAG, "onCreatePref: "+binding.etName?.text.toString())
            sharedPreference.putString(Constants.KEY_EMAIL, binding.etEmail?.text.toString())
            sharedPreference.putString(Constants.KEY_MOBILE_NUMBER,binding.etNumber.text.toString())

//            sharedPreference.putInt(Constants.KEY_IMAGE,image.toInt())
            sharedPreference.putBoolean(
                Constants.KEY_GENDER,
                binding.radiogrp?.checkedRadioButtonId == R.id.radioBtnMale,
            )

            if (binding.etName?.text.toString().isEmpty()) {

                Toast.makeText(this, "Please Enter Your Name", Toast.LENGTH_SHORT).show()
            } else if (binding.etEmail?.text.toString().isEmpty()) {
                Toast.makeText(this, "Please Enter Your Email", Toast.LENGTH_SHORT).show()

            } else if (binding.etNumber.text.toString().isEmpty()) {
                Toast.makeText(this, "Please Enter Your Number", Toast.LENGTH_SHORT).show()
            } else if (binding.etNumber.length() < 10) {
                Toast.makeText(
                    this@EditProfileActivity,
                    "Mobile Number should be  10 or more character",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (binding.radiogrp.isEmpty()) {

                Toast.makeText(this, "Please Select Gender", Toast.LENGTH_SHORT).show()
            } else {
                userUpdateApi()
            }
            Toast.makeText(applicationContext, "Data Saved!", Toast.LENGTH_SHORT).show()
        }

        binding.etName?.setText(sharedPreference.getString(Constants.KEY_FULL_NAME))
        Log.d(TAG, "onCreatePref: "+Constants.KEY_FULL_NAME)

        binding.etEmail?.setText(sharedPreference.getString(Constants.KEY_EMAIL))
        binding.etNumber?.setText(sharedPreference.getString(Constants.KEY_MOBILE_NUMBER).toString())
//        binding.imgCivProfile.setImageResource(sharedPreference.getInt(Constants.KEY_IMAGE))

        if (sharedPreference.getBoolean(Constants.KEY_GENDER)){
            binding.radioBtnMale?.isChecked = true
        }else{
            binding.radioBtnFeMale?.isChecked = true
        }
        Toast.makeText(applicationContext, "Data Retrieved!", Toast.LENGTH_SHORT).show()

        binding.igEditProfile.setOnClickListener {
            if (!HasPermission(this@EditProfileActivity, PERMISSIONS)) {
                ActivityCompat.requestPermissions(this@EditProfileActivity, PERMISSIONS, 1)
            } else {
                selectImage()
            }

            binding.btnCancel.setOnClickListener {
                var intent = Intent(this@EditProfileActivity,SettingActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun selectImage() {
        val dialog = Dialog(this@EditProfileActivity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.editprofiledialog)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))


        val txt_cancel = dialog.findViewById<TextView>(R.id.txt_cancel)
        val ig_camera = dialog.findViewById<ImageView>(R.id.ig_camera)
        val ig_gallery = dialog.findViewById<ImageView>(R.id.ig_gallery)



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
        txt_cancel.setOnClickListener { dialog.dismiss() }

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
                    .into(binding.imgCivProfile!!)
            }
            1 -> if (resultCode == RESULT_OK) {
                val selectedImage = data?.data
                val finalFile: File = File(selectedImage?.let { getRealPathFromURI(it) })
                Log.e("TAG", "gallery----->$finalFile")
                APP_FILE = finalFile
                Glide.with(this)
                    .load(finalFile.path)
                    .error(R.drawable.ig_profile)
                    .into(binding.imgCivProfile)
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

    private fun HasPermission(
        editProfileActivity: EditProfileActivity,
        permissions: Array<String>
    ): Boolean {
        if (editProfileActivity != null && permissions != null) {
            for (permission in permissions) {
                if (ActivityCompat.checkSelfPermission(
                        editProfileActivity,
                        permission
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return false
                }
            }
        }
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                selectImage()
            } else {
                // Toast.makeText(this,"calling permission is denied",Toast.LENGTH_SHORT).show();
                AlertDialog.Builder(this)
                    .setTitle("Permission")
                    .setMessage("Allow to access your camera and storage?")
                    .setPositiveButton(
                        android.R.string.yes
                    ) { dialog, which ->
                        val i = Intent(
                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.parse("package:" + BuildConfig.APPLICATION_ID)
                        )
                        startActivity(i)
                    }
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show()
            }
        }
    }


    private fun userUpdateApi() {

        val sharedPreference = getSharedPreferences("APP_PREF", Context.MODE_PRIVATE)

        val hUserId = sharedPreference.getString("hostId", "Id")


        Log.d(TAG, "onIdUser: " + hUserId)
        val requestData = HashMap<String, RequestBody>()

        if (APP_FILE != null) {
            mPart = prepareFilePart("profileImage", APP_FILE!!)
            requestData.put("fullName", createPartFromString(binding.etName!!.text.toString()))
            requestData.put("_id", createPartFromString(hUserId!!))
            requestData.put("mobile", createPartFromString(binding.etNumber.text.toString()))
            requestData.put("email", createPartFromString(binding.etEmail.text.toString()))
            requestData.put("gender", createPartFromString(isGender!!))
            updateUserWithProfile(mPart!!, requestData);
        } else {
            requestData.put("fullName", createPartFromString(binding.etName!!.text.toString()))
            requestData.put("_id", createPartFromString(hUserId!!))
            requestData.put("mobile", createPartFromString(binding.etNumber.text.toString()))
            requestData.put("email", createPartFromString(binding.etEmail.text.toString()))
            requestData.put("gender", createPartFromString(isGender!!))
//            requestData.put("_id", createPartFromString(Str_id!!))

            mPart = null
            updateUserWithProfile(mPart, requestData);
        }
    }

    private fun updateUserWithProfile(
        mPart: MultipartBody.Part?,
        requestData: HashMap<String, RequestBody>
    ) {
        if (Utils.checkNetwork(applicationContext)) {

            val apiInterface: ApiInterface = ApiUtility.getUser().create(ApiInterface::class.java)
            var call: Call<UserUpdateResponse>? = null

            if (mPart != null) {
                call = apiInterface.updateUserP(mPart, requestData)
            } else {
                call = apiInterface.updateUser(requestData)
            }

            call.enqueue(object : Callback<UserUpdateResponse> {
                override fun onResponse(
                    call: Call<UserUpdateResponse>,
                    response: Response<UserUpdateResponse>
                ) {
                    Toast.makeText(this@EditProfileActivity, "Api call  ", Toast.LENGTH_SHORT)
                        .show()

                    if (response.isSuccessful) {

                        userUpdateResponse = response.body()

                        if (userUpdateResponse?.meta?.code == 200) {
                            val data = UserUpdateResponse.Data(
                                email = signupResponse?.email ?: "",
                                fullName = signupResponse?.fullName ?: "",
                                gender = signupResponse?.gender ?: "",
                                mobile = signupResponse?.mobile ?: ""
                            )

//                            Session.setCurrentUser(loginResponse!!)
                            val intent =
                                Intent(this@EditProfileActivity, SettingActivity::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(
                                this@EditProfileActivity,
                                "" + response.body()?.meta?.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    } else {
                        Toast.makeText(this@EditProfileActivity, "Failed", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                override fun onFailure(call: Call<UserUpdateResponse>, t: Throwable) {

                }
            })
        } else {
            Toast.makeText(this, "please check your internet connection ", Toast.LENGTH_SHORT)
                .show()
        }

    }

    private fun createPartFromString(toString: Any): RequestBody {
        return RequestBody.create(MultipartBody.FORM, "" + toString)
    }

    private fun prepareFilePart(s: String, appFile: File): MultipartBody.Part? {
        val MEDIA_TYPE_PNG: MediaType = "image/*".toMediaTypeOrNull()!!
        val requestFile: RequestBody = RequestBody.create(MEDIA_TYPE_PNG, appFile)
        return MultipartBody.Part.createFormData(s, appFile.getName(), requestFile)
    }
}