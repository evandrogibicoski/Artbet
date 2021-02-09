package com.glance.artbet.utils.extensions.android

import io.reactivex.Single

sealed class Result<T>
data class Success<T>(val value: T, val wasResent: Boolean = false) : Result<T>()
data class Failure<T>(val errorMessage: String, val errorCode: Int) : Result<T>()
data class Loading<T>(val isLoading: Boolean) : Result<T>()

data class ConnectionError<T>(val request: Single<T>, var customErrorMessage: String? = null) : Result<T>()
data class AuthError<T>(val request: Single<T>) : Result<T>()
