package com.glance.artbet.di.components

import com.glance.artbet.di.modules.ActivityModule
import com.glance.artbet.di.scopes.ActivityScope
import com.glance.artbet.mvvm.BaseViewModel
import com.glance.artbet.ui.base.BaseActivity
import dagger.Subcomponent


@ActivityScope
@Subcomponent(
    modules = [
        ActivityModule::class,
        /*
        ViewModelFactoryModule::class,
        ActivityViewModule::class,
        GeneralViewModelModule::class,
        AuthViewModelModule::class,
        MainViewModelModule::class
        */
    ]
)
interface ActivityComponent {
    fun inject(target: BaseActivity<BaseViewModel>)
    fun inject(target: BaseFragment<BaseViewModel>)
    fun inject(target: BaseDialogFragment<BaseViewModel>)
}