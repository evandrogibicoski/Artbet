package com.glance.artbet.di.modules.view_model

import androidx.lifecycle.ViewModel
import com.glance.artbet.di.map_key.ViewModelKey
import com.glance.artbet.di.scopes.ActivityScope
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ActivityViewModelModule {
    @Binds
    @ActivityScope
    @IntoMap
    @ViewModelKey(MainActivityViewModel::class)
    abstract fun bindMainActivityViewModel(model: MainActivityViewModel): ViewModel

    @Binds
    @ActivityScope
    @IntoMap
    @ViewModelKey(SplashActivityViewModel::class)
    abstract fun bindSplashActivityViewModel(model: SplashActivityViewModel): ViewModel

    @Binds
    @ActivityScope
    @IntoMap
    @ViewModelKey(AuthorizationActivityViewModel::class)
    abstract fun bindAuthorizationActivityViewModel(model: AuthorizationActivityViewModel): ViewModel

    @Binds
    @ActivityScope
    @IntoMap
    @ViewModelKey(DemoActivityViewModel::class)
    abstract fun bindDemoActivityViewModel(model: DemoActivityViewModel): ViewModel
}