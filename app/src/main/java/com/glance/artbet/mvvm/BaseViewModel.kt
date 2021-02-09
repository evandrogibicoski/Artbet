package com.glance.artbet.mvvm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.glance.artbet.ArtbetApp
import com.glance.artbet.ui.models.TransitionAnimationStatus
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

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



}