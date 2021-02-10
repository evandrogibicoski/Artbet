package com.glance.artbet.di.modules

import androidx.lifecycle.ViewModelProvider
import com.glance.artbet.di.scopes.ActivityScope
import com.glance.artbet.mvvm.DaggerViewModelFactory
import dagger.Binds
import dagger.Module

@Module
abstract class ViewModelFactoryModule {
    @Binds
    @ActivityScope
    abstract fun bindViewModelFactory(viewModelFactory: DaggerViewModelFactory): ViewModelProvider.Factory
}