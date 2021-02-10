package com.glance.artbet.domain.repository.user_api

import android.content.Context
import android.net.Uri
import com.glance.artbet.domain.repository.BaseRepository
import io.reactivex.Single
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

class UserApiRepository @Inject constructor(
        context: Context,
        private val userApiInterface: UserApiInterface
) : BaseRepository(context) {

//    fun login(
//            email: String,
//            password: String,
//            onResponse: (Result<LoginResponse>) -> Unit
//    ): Single<LoginResponse> {
//        val loginRequest = LoginRequest(email, password)
//
//        return userApiInterface.login(loginRequest)
//                .getWrapped<LoginResponse, LoginResponse> { onResponse(it) }
//    }
//
//    fun signInToFirebase(
//            firebaseToken: String,
//            onResponse: (Result<AuthResult>) -> Unit
//    ): Single<AuthResult> {
//        return FirestoreChat.signInWithCustomToken(firebaseToken).getWrapped(onResponse)
//    }
//
//    fun signOutFromFirebase() {
//        FirestoreChat.signOut()
//    }
//
//    fun getUser(
//            onResponse: (Result<UserProfile>) -> Unit
//    ): Single<UserProfile> {
//        return userApiInterface.getUser()
//                .getWrapped<UserProfile, UserProfile> { onResponse(it) }
//    }
//
//    fun updatePassword(
//            password: String,
//            onResponse: (Result<Unit>) -> Unit
//    ): Single<Unit> {
//        val updatePasswordRequest = UpdatePasswordRequest(password)
//
//        return userApiInterface.updatePassword(updatePasswordRequest)
//                .getWrapped<Unit, Unit> { onResponse(it) }
//    }
//
//    fun renewPassword(
//            email: String,
//            password: String,
//            onResponse: (Result<Unit>) -> Unit
//    ): Single<Unit> {
//        val renewPasswordRequest = RenewPassRequest(email, password)
//
//        return userApiInterface.renewPassword(renewPasswordRequest)
//                .getWrapped<Unit, Unit> { onResponse(it) }
//    }


    fun sendEmail(
            email: String,
            onResponse: (Result<Unit>) -> Unit
    ): Single<Unit> {
        return userApiInterface.sendEmail(email)
                .getWrapped<Unit, Unit> { onResponse(it) }
    }

    fun checkCode(
            email: String,
            code: String,
            onResponse: (Result<Unit>) -> Unit
    ): Single<Unit> {
        return userApiInterface.checkCode(email, code)
                .getWrapped<Unit, Unit> { onResponse(it) }
    }

    fun deleteDeviceId(
            onResponse: (Result<Unit>) -> Unit
    ): Single<Unit> {
        return userApiInterface.deleteDeviceId().getWrapped(onResponse)
    }
}
