package com.glance.artbet.di.modules

import com.glance.artbet.di.scopes.ActivityScope
import com.glance.artbet.ui.base.BaseActivity
import dagger.Module
import dagger.Provides

@Module
class ActivityModule (private val baseActivity: BaseActivity<*>) {

    @Provides
    @ActivityScope
    fun provideBaseActivity(): BaseActivity<*> = baseActivity
}