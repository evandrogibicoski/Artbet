package com.glance.artbet.di.components

import com.glance.artbet.di.modules.ActivityModule
import com.glance.artbet.di.modules.ViewModelFactoryModule
import com.glance.artbet.di.modules.view_model.ActivityViewModelModule
import com.glance.artbet.di.modules.view_model.GeneralViewModelModule
import com.glance.artbet.di.modules.view_model.fragment.AuthViewModelModule
import com.glance.artbet.di.modules.view_model.fragment.MainViewModelModule
import com.glance.artbet.di.scopes.ActivityScope
import com.glance.artbet.mvvm.BaseViewModel
import com.glance.artbet.ui.base.BaseActivity
import com.glance.artbet.ui.base.BaseDialogFragment
import com.glance.artbet.ui.base.BaseFragment
import dagger.Subcomponent


@ActivityScope
@Subcomponent(
    modules = [
        ActivityModule::class,
        ViewModelFactoryModule::class,
        ActivityViewModelModule::class,
        GeneralViewModelModule::class,
        AuthViewModelModule::class,
        MainViewModelModule::class
    ]
)
interface ActivityComponent {
    fun inject(target: BaseActivity<BaseViewModel>)
    fun inject(target: BaseFragment<BaseViewModel>)
    fun inject(target: BaseDialogFragment<BaseViewModel>)
}