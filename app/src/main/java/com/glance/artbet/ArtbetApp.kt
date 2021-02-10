package com.glance.artbet

import android.app.Activity
import android.app.Application
import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.glance.artbet.data.local_storage.LocalStorageRepository
import com.glance.artbet.di.components.AppComponent
import com.glance.artbet.di.components.DaggerAppComponent
import com.glance.artbet.di.modules.AppModule
import com.glance.artbet.di.modules.RetrofitApiModule

class ArtbetApp : Application(), LifecycleObserver {
    companion object {
        operator fun get(activity: Activity): ArtbetApp {
            return activity.application as ArtbetApp
        }

        operator fun get(context: Context): ArtbetApp {
            return context as ArtbetApp
        }

        lateinit var context: Context
    }

    var isAppBackgrounded = false

    override fun onCreate() {
        super.onCreate()

        ProcessLifecycleOwner.get().lifecycle.addObserver(this)

        context = applicationContext
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onAppBackgrounded() {
        isAppBackgrounded = true
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onAppForegrounded() {
        isAppBackgrounded = false
    }

    val appComponent: AppComponent by lazy {
        initDagger()
    }

    private fun initDagger(): AppComponent =
        DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .retrofitApiModule(RetrofitApiModule(LocalStorageRepository(this)))
            .build()

}