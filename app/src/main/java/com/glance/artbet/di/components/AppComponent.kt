package com.glance.artbet.di.components

import android.app.Application
import com.glance.artbet.di.modules.ActivityModule
import com.glance.artbet.di.modules.AppModule
import com.glance.artbet.di.modules.RepositoryModule
import com.glance.artbet.di.modules.RetrofitApiModule
import com.glance.artbet.mvvm.BaseViewModel
import com.glance.artbet.domain.repository.CallbackWrapper
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AppModule::class,
    RetrofitApiModule::class,
    RepositoryModule::class,
//    DatabaseModule::class
])
interface AppComponent {
    fun plus(activityModule: ActivityModule): ActivityComponent

    fun inject(app: Application)
    fun inject(target: BaseViewModel)
    fun inject(target: CallbackWrapper<Any, Any>)
    //fun inject(target: OneSignalNotificationHandlerService)
}