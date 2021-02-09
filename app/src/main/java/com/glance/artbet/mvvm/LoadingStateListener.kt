package com.glance.artbet.mvvm

import com.glance.artbet.utils.extensions.android.ConnectionError
import com.glance.artbet.utils.extensions.android.Failure
import com.glance.artbet.utils.extensions.android.Success

interface LoadingStateListener {
    fun onStartLoading()
    fun onStopLoading()
    fun onSuccessfullyResent(success: Success<*>)
    fun onError(failure: Failure<*>)
    fun onConnectionError(connectionError: ConnectionError<*>)
}