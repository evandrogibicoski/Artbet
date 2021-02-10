package com.glance.artbet.domain.repository

import android.content.Context
import com.glance.artbet.ArtbetApp
import com.glance.artbet.R
import com.glance.artbet.data.response.BaseResponse
import com.glance.artbet.utils.extensions.android.*
import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.Retrofit
import java.lang.ref.WeakReference
import java.net.ConnectException
import javax.inject.Inject

class CallbackWrapper<T, V>(
        private val contextWeakReference: WeakReference<Context>? = null,
        private var onResult: (Result<V>) -> Unit
) {

    @Inject
    lateinit var retrofit: Retrofit

    var request: Single<T>? = null
    private var repeatingUnauthorizedRequestCount = 0

    init {
        contextWeakReference?.get()?.let {
            ArtbetApp[it].appComponent.inject(this as CallbackWrapper<Any, Any>)
        }
    }

    private var wasResent = false

    fun onSuccess(response: T) {
        onResult(Success(response as V, wasResent))
    }

    fun onError(t: Throwable) {
        val errorMessage: String
        val errorCode: Int
        wasResent = false
        when (t) {
            is ConnectException -> {
                errorMessage =
                    contextWeakReference?.get()?.getString(R.string.error_no_internet_connection) ?: ""
                errorCode =
                    ApiErrors.ERROR_INTERNET_CONNECTION_CODE.code
                request?.let {
                    wasResent = true
                    onResult(ConnectionError(it as Single<V>))
                    return
                }
            }
            is HttpException -> when (t.code()) {
                ApiErrors.ERROR_UNAUTHORIZED.code -> {
                    request?.let {
                        wasResent = true
                        onResult(AuthError(it as Single<V>))
                        return
                    }
                    return
                }
                else -> {
                    val errorWrapper = ErrorWrapper()
                    val error = t.response()?.let { errorWrapper.parseError(retrofit, it) }

                    if (error != null) {
                        errorMessage = error.error ?: ""
                        errorCode = error.code ?: t.code()
                    } else {
                        errorMessage = t.message ?: t.toString()
                        errorCode =
                            ApiErrors.ERROR_UNEXPECTED_CODE.code
                    }
                }
            }
            is IllegalStateException -> {
                errorMessage =
                    contextWeakReference?.get()?.getString(R.string.error_converting_data) ?: ""
                errorCode =
                    ApiErrors.ERROR_CONVERTING_DATA_CODE.code
            }
            else -> {
                errorMessage = t.message ?: t.toString()
                errorCode = ApiErrors.ERROR_UNEXPECTED_CODE.code
            }
        }
        onResult(Failure(errorMessage, errorCode))
    }

    private inner class ErrorWrapper {
        fun parseError(retrofit: Retrofit, response: Response<*>): BaseResponse? {
            val converter: Converter<ResponseBody, BaseResponse> = retrofit
                .responseBodyConverter(BaseResponse::class.java, emptyArray())
            try {
                response.errorBody()?.let {
                    return converter.convert(it)
                }
            } catch (e: Exception) {
                return null
            }
            return null
        }
    }
}