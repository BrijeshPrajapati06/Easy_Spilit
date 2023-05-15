package com.diyantech.easysplit.ApiKtService

import com.diyantech.easysplit.modelclass.request.*
import com.diyantech.easysplit.modelclass.response.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*
import java.util.HashMap


interface ApiInterface {

    @POST("user/logIn")
    fun sendData(
        @Body loginRequest: LoginRequest
    ): Call<LoginResponse>

 /*   @Multipart
    @POST("user/userSaveData")
    fun saveDatap(
        @PartMap param: HashMap<String, RequestBody>
    ): Call<SignupResponse>

    @Multipart
    @POST("user/userSaveData")
    fun saveData(
        @Part filepart: MultipartBody.Part, @PartMap param: HashMap<String, RequestBody>
    ): Call<SignupResponse>*/

    @POST("user/userSaveData")
    fun saveData(
        @Body signupRequest: SignupRequest
    ): Call<SignupResponse>

    @POST("user/forgotPassworForUser")
    fun sendOtp(
        @Body forgotPasswordRequest: ForgotPasswordRequest
    ): Call<ForgotPasswordResponse>

    @POST("user/checkOtpVerificationForUser")
    fun sendOtpVerify(
        @Body otpRequest: OtpRequest
    ): Call<OtpResponse>

    @POST("user/resendOtpUser")
    fun reSendOtpVerify(
        @Body resendOtpRequest: ResendOtpRequest
    ): Call<ResendOtpResponse>

    @POST("user/passwordResetForUser")
    fun userResetPassword(
        @Body createPassworsRequest: CreatePassworsRequest
    ): Call<CreatePasswordResponse>

    @POST("event/eventCreate")
    fun eventCreate(
        @Body eventCreateRequest: EventCreateRequest,
//        @Query("myToken") Token: String
    ): Call<EventCreateResponse>

    @GET("event/eventList")
    fun getEventData(
        @Query("hostId") Id: String,
        @Query("page") page:Int
    ): Call<GetEventPageDataResponse>

    @Multipart
    @POST("user/userUpdateData")
    fun updateUser(@PartMap param: HashMap<String, RequestBody>) : Call<UserUpdateResponse>

    @Multipart
    @POST("user/userUpdateData")
    fun updateUserP(@Part filepart: MultipartBody.Part, @PartMap param: HashMap<String, RequestBody>): Call<UserUpdateResponse>

    @GET("user/currencyDropdownList")
    fun getCurrencyDropListData(): Call<CurrencyDropListResponse>

    @GET("currency/currencyListData")
    fun getCurrencyData(
        @Query("_id") Id: String,
    ): Call<GetCurrencyDataListResponse>

    @POST("user/changePasswordForUser")
    fun userChangePassword(
        @Body changePasswordRequest: ChangePasswordRequest
    ): Call<ChangePasswordResponse>

    @GET("eventUser/userList")
    fun getUserData(
        @Query("eventId") Id: String,
        @Query("page") page:Int
    ): Call<GetUserListResponse>
}