package com.glance.artbet.domain.repository.user_api

import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.http.*


interface UserApiInterface {
//    @POST("/user/login")
//    fun login(@Body request: LoginRequest): Single<LoginResponse>
//
//    @GET("/user")
//    fun getUser(): Single<UserProfile>
//
//    @HTTP(method = "DELETE", path = "/user", hasBody = true)
//    fun deleteUser(@Body request: DeleteUserRequest): Single<Unit>

    @POST("/user/device")
    fun updateDeviceId(@Query("device_id") deviceId: String): Single<Unit>

//    @PATCH("/user/update_password")
//    fun updatePassword(@Body request: UpdatePasswordRequest): Single<Unit>
//
//    @PATCH("/user")
//    fun updateUserProfile(@Body request: UpdateUserProfileRequest): Single<UpdateUserProfileResponse>
//
//    @Multipart
//    @PATCH("/user/image")
//    fun updateUserProfileImage(@Part filePart: MultipartBody.Part?): Single<UpdateUserProfileResponse>

    @POST("/user/send_code")
    fun sendEmail(@Query("email") email: String): Single<Unit>

    @POST("/user/check_code")
    fun checkCode(@Query("email") email: String, @Query("code") code: String): Single<Unit>

    @DELETE("/user/device")
    fun deleteDeviceId(): Single<Unit>

    @GET
    fun downloadFile(@Url url: String): Single<ResponseBody>
}
