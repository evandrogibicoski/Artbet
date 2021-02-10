package com.glance.artbet.di.modules.view_model.fragment

import androidx.lifecycle.ViewModel
import com.glance.artbet.di.scopes.ActivityScope
import com.glance.artbet.di.map_key.ViewModelKey
import com.glance.artbet.ui.fragments.auth.change_name.ChangeNameFragmentViewModel
import com.glance.artbet.ui.fragments.auth.change_password.ChangePasswordFragmentViewModel
import com.glance.artbet.ui.fragments.auth.forgot_password.ForgotPasswordChangePassFragmentViewModel
import com.glance.artbet.ui.fragments.auth.forgot_password.ForgotPasswordEmailFragmentViewModel
import com.glance.artbet.ui.fragments.auth.forgot_password_code.ForgotPasswordCodeFragmentViewModel
import com.glance.artbet.ui.fragments.auth.notifications_prompt.NotificationsPromptFragmentViewModel
import com.glance.artbet.ui.fragments.auth.password_changed.PasswordChangedFragmentViewModel
import com.glance.artbet.ui.fragments.auth.sign_in.LoginFragmentViewModel
import com.glance.artbet.ui.fragments.tutorial.TutorialMainFragmentViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class AuthViewModelModule {

    @Binds
    @ActivityScope
    @IntoMap
    @ViewModelKey(LoginFragmentViewModel::class)
    abstract fun bindLoginFragmentViewModel(model: LoginFragmentViewModel): ViewModel

    @Binds
    @ActivityScope
    @IntoMap
    @ViewModelKey(ChangePasswordFragmentViewModel::class)
    abstract fun bindChangePasswordFragmentViewModel(model: ChangePasswordFragmentViewModel): ViewModel

    @Binds
    @ActivityScope
    @IntoMap
    @ViewModelKey(PasswordChangedFragmentViewModel::class)
    abstract fun bindPasswordChangedFragmentViewModel(model: PasswordChangedFragmentViewModel): ViewModel

    @Binds
    @ActivityScope
    @IntoMap
    @ViewModelKey(ChangeNameFragmentViewModel::class)
    abstract fun bindChangeNameFragmentViewModel(model: ChangeNameFragmentViewModel): ViewModel

    @Binds
    @ActivityScope
    @IntoMap
    @ViewModelKey(TutorialMainFragmentViewModel::class)
    abstract fun bindTutorialMainFragmentViewModel(model: TutorialMainFragmentViewModel): ViewModel

    @Binds
    @ActivityScope
    @IntoMap
    @ViewModelKey(ForgotPasswordEmailFragmentViewModel::class)
    abstract fun bindForgotPasswordEmailFragmentViewModel(model: ForgotPasswordEmailFragmentViewModel): ViewModel

    @Binds
    @ActivityScope
    @IntoMap
    @ViewModelKey(ForgotPasswordCodeFragmentViewModel::class)
    abstract fun bindForgotPasswordCodeFragmentViewModel(model: ForgotPasswordCodeFragmentViewModel): ViewModel

    @Binds
    @ActivityScope
    @IntoMap
    @ViewModelKey(ForgotPasswordChangePassFragmentViewModel::class)
    abstract fun bindForgotPasswordChangePassFragmentViewModel(model: ForgotPasswordChangePassFragmentViewModel): ViewModel

    @Binds
    @ActivityScope
    @IntoMap
    @ViewModelKey(NotificationsPromptFragmentViewModel::class)
    abstract fun bindNotificationsPromptFragmentViewModel(model: NotificationsPromptFragmentViewModel): ViewModel

}
