package com.glance.artbet.domain.repository

import android.content.Context
import android.util.Base64
import com.glance.artbet.utils.extensions.android.Loading
import com.glance.artbet.utils.extensions.android.Result
import com.glance.artbet.utils.extensions.getSchedulers
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject
import java.io.File
import java.lang.ref.WeakReference


abstract class BaseRepository(protected val context: Context) {

    private fun <T, V> initRequestSingleForResult(
        request: Single<T>,
        onResult: (Result<V>) -> Unit
    ): Single<T> {
        val callback = CallbackWrapper<T, V>(
            WeakReference(context),
            onResult
        )
        val requestSingle = request.getSchedulers()
            .doOnSubscribe { onResult(Loading(true)) }
            .doOnSuccess { callback.onSuccess(it) }
            .doOnError { callback.onError(it) }
            .doFinally { onResult(Loading(false)) }
        callback.request = requestSingle
        return requestSingle
    }

    protected fun <T, V> Single<T>.getWrapped(onResult: (Result<V>) -> Unit): Single<T> {
        return initRequestSingleForResult(this, onResult)
    }

//    protected fun <T> Observable<T>.getWrappedByFirebase(
//        onResult: (Result<T>) -> Unit
//    ): Observable<T> {
//        return doOnSubscribe { onResult(Loading(true)) }
//            .doOnNext { onResult(Success(it)) }
//            .doOnError { onResult(parseFirebaseError(it)) }
//            .doOnEach { onResult(Loading(false)) }
//            .doFinally { onResult(Loading(false)) }
//    }
//
//    protected fun <T> Single<T>.getWrappedByFirebase(
//        onResult: (Result<T>) -> Unit
//    ): Single<T> {
//        return doOnSubscribe { onResult(Loading(true)) }
//            .doOnSuccess { onResult(Success(it)) }
//            .doOnError { onResult(parseFirebaseError(it)) }
//            .doFinally { onResult(Loading(false)) }
//    }

//    fun <T> parseFirebaseError(t: Throwable): Failure<T> {
//        return when {
//            t is FirebaseFirestoreException && t.code == FirebaseFirestoreException.Code.UNAVAILABLE -> {
//                val errorMessage =
//                    context.getString(R.string.error_no_internet_connection)
//                val errorCode =
//                    ApiErrors.ERROR_INTERNET_CONNECTION_CODE.code
//                Failure(errorMessage, errorCode)
//            }
//            t is FirebaseFirestoreException && t.code.value()
//                    == FirebaseFirestoreException.Code.PERMISSION_DENIED.value() -> {
//                Failure(t.message ?: t.toString(), ApiErrors.ERROR_UNAUTHORIZED.code)
//            }
//            else -> Failure(t.message ?: t.toString(), 0)
//        }
//    }

    protected fun zip(requests: ArrayList<Single<*>>, onLoadingEnded: () -> Unit): Disposable {
        return Single.zip(requests) { }.subscribe({ onLoadingEnded() }, {})
    }

    protected fun getJson(data: Map<String, Any>): RequestBody {
        return RequestBody.create(
            MediaType.parse("application/json; charset=utf-8"),
            (JSONObject(data)).toString()
        )
    }

    protected fun File.getBase64String() =
        Base64.encodeToString(readBytes(), Base64.DEFAULT)

}