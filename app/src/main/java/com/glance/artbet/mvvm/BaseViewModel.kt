package com.glance.artbet.mvvm

import android.app.Application
import android.app.NotificationManager
import android.content.Context
import android.util.Log
import androidx.annotation.StringRes
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.glance.artbet.ArtbetApp
import com.glance.artbet.ui.models.TransitionAnimationStatus
import com.glance.artbet.utils.extensions.android.*
import com.glance.artbet.utils.extensions.getSchedulers
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import javax.inject.Inject
import kotlin.Result

abstract class BaseViewModel(app: Application) : AndroidViewModel(app), LoadingStateListener {

    @Inject
    lateinit var compositeDisposable: CompositeDisposable

    val loadingStateData = MutableLiveData<Result<*>>()

    val transitionAnimationStatus = MutableLiveData<TransitionAnimationStatus>()

    init {
        ArtbetApp[app].appComponent.inject(this)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    protected fun Completable.call() = this.subscribe({}, ::onError).addToDispose()
    protected fun Completable.call(onSuccess: () -> Unit) = this.getSchedulers()
        .subscribe(onSuccess, ::onError).addToDispose()

    protected fun <T> Single<T>.call() = this.subscribe({}, {}).addToDispose()
    protected fun <T> Single<T>.call(onSuccess: (t: T) -> Unit) = this.getSchedulers()
        .subscribe(onSuccess, ::onError).addToDispose()

    protected fun <T> Observable<T>.call() =
        this.subscribe({}, ::onError).addToDispose()
    protected fun <T> Observable<T>.call(onNext: (t: T) -> Unit) =
        this.subscribe(onNext, ::onError).addToDispose()

    protected fun Disposable.addToDispose() {
        compositeDisposable.add(this)
    }


    protected inline fun <T> Result<T>.unWrapResult(
        customErrorString: String? = null,
        onSuccess: (Success<T>) -> Unit = {}
    ) {
        when (this) {
            is Loading<*> -> {
                if (this.isLoading) onStartLoading()
                else onStopLoading()
            }
            is Failure<*> -> {
                customErrorString?.let {
                    onError(Failure<T>(it, this.errorCode))
                } ?: onError(this)
            }
            is Success<*> -> {
                onSuccess(this)
            }
            is ConnectionError<*> -> {
                this.customErrorMessage = customErrorString
                onConnectionError(this)
            }
            is AuthError<*> -> onAuthError(this)
        }
    }

    protected inline fun <T> Result<T>.unWrapResult(
        customErrorString: String? = null,
        shouldShowLoading: Boolean = true,
        onSuccess: (Success<T>) -> Unit = {}
    ) {
        when (this) {
            is Loading<*> -> {
                if (shouldShowLoading) {
                    if (this.isLoading) onStartLoading()
                    else onStopLoading()
                }
            }
            is Failure<*> -> {
                customErrorString?.let {
                    onError(Failure<T>(it, this.errorCode))
                } ?: onError(this)
            }
            is Success<*> -> {
                onSuccess(this)
            }
            is ConnectionError<*> -> {
                this.customErrorMessage = customErrorString
                onConnectionError(this)
            }
            is AuthError<*> -> onAuthError(this)
        }
    }


    protected fun onAuthError(authError: AuthError<*>) {
        loadingStateData.value = authError
    }

    override fun onStartLoading() {
        loadingStateData.value = Loading<Any>(true)
    }

    override fun onStopLoading() {
        try {
            loadingStateData.value = Loading<Any>(false)
        }
        catch (e: IllegalStateException){
            loadingStateData.postValue(Loading<Any>(false))
        }
    }

    override fun onError(failure: Failure<*>) {
        Log.d("ERROR", "${failure.errorMessage}, code ${failure.errorCode}")
        loadingStateData.value = failure
    }

    override fun onConnectionError(connectionError: ConnectionError<*>) {
        loadingStateData.value = connectionError
    }

    override fun onSuccessfullyResent(success: Success<*>) {
        loadingStateData.value = success
    }

    protected fun onError(throwable: Throwable) {
        onError(Failure<Any>(throwable.message ?: throwable.toString(), 0))
    }

    protected fun getString(@StringRes stringResourceId: Int, vararg format: Any): String {
        return if (format.isNotEmpty())
            getApplication<ArtbetApp>().getString(stringResourceId, *format)
        else
            getApplication<ArtbetApp>().getString(stringResourceId)
    }

    fun setTransitionAnimationStatus(status: TransitionAnimationStatus) {
        transitionAnimationStatus.postValue(status)
    }

}