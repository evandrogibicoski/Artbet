package com.glance.artbet.ui.fragments.connection_error

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.glance.artbet.mvvm.BaseViewModel
import com.glance.artbet.utils.extensions.android.ConnectionError
import com.glance.artbet.utils.extensions.android.Failure
import java.net.ConnectException
import javax.inject.Inject

class ConnectionErrorFragmentViewModel @Inject constructor(app: Application) : BaseViewModel(app) {

    private val connectionErrorData = MutableLiveData<ConnectionError<*>>()
    val resentSuccessfullyData = MutableLiveData<Boolean>()

    fun putRequest(request: ConnectionError<*>) {
        connectionErrorData.postValue(request)
    }

    fun setClosed() {
        resentSuccessfullyData.postValue(true)
    }

    fun callRequest() {
        val failedRequest = connectionErrorData.value

        if (failedRequest == null) {
            onError(Failure<Any>("Request not found", 0))
        } else {
            failedRequest.request
                .doOnSubscribe { onStartLoading() }
                .doOnSuccess { setClosed() }
                .doOnError {
                    if (it !is ConnectException) {
                        setClosed()
                        onError(it)
                    }
                }
                .doFinally { onStopLoading() }
                .call()
        }
    }

    fun clear() {
        connectionErrorData.value = null
        resentSuccessfullyData.value = null
    }
}