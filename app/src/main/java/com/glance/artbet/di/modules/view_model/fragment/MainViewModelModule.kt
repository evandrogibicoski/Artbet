package com.glance.artbet.di.modules.view_model.fragment

import androidx.lifecycle.ViewModel
import com.glance.artbet.di.map_key.ViewModelKey
import com.glance.artbet.di.scopes.ActivityScope
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class MainViewModelModule {

    /**
     * Profile
     */
    @Binds
    @ActivityScope
    @IntoMap
    @ViewModelKey(ProfileFragmentViewModel::class)
    abstract fun bindProfileFragmentViewModel(model: ProfileFragmentViewModel): ViewModel

    @Binds
    @ActivityScope
    @IntoMap
    @ViewModelKey(NeedToChangeSomethingFragmentViewModel::class)
    abstract fun bindNeedToChangeSomethingFragmentViewModel(model: NeedToChangeSomethingFragmentViewModel): ViewModel

    @Binds
    @ActivityScope
    @IntoMap
    @ViewModelKey(ProfileChangePasswordFragmentViewModel::class)
    abstract fun bindProfileChangePasswordFragmentViewModel(model: ProfileChangePasswordFragmentViewModel): ViewModel

    @Binds
    @ActivityScope
    @IntoMap
    @ViewModelKey(ProfilePasswordChangedFragmentViewModel::class)
    abstract fun bindProfilePasswordChangedFragmentViewModel(model: ProfilePasswordChangedFragmentViewModel): ViewModel

    @Binds
    @ActivityScope
    @IntoMap
    @ViewModelKey(ProfileChangeOtherNameFragmentViewModel::class)
    abstract fun bindProfileChangeOtherNameFragmentViewModel(model: ProfileChangeOtherNameFragmentViewModel): ViewModel

    @Binds
    @ActivityScope
    @IntoMap
    @ViewModelKey(ProfileOtherNameChangedFragmentViewModel::class)
    abstract fun bindProfileOtherNameChangedFragmentViewModel(model: ProfileOtherNameChangedFragmentViewModel): ViewModel

    @Binds
    @ActivityScope
    @IntoMap
    @ViewModelKey(DeleteAccountSharedViewModel::class)
    abstract fun bindDeleteAccountSharedViewModel(model: DeleteAccountSharedViewModel): ViewModel

    @Binds
    @ActivityScope
    @IntoMap
    @ViewModelKey(ProvideFeedbackFragmentViewModel::class)
    abstract fun bindProvideFeedbackFragmentViewModel(model: ProvideFeedbackFragmentViewModel): ViewModel

    @Binds
    @ActivityScope
    @IntoMap
    @ViewModelKey(CropAvatarFragmentViewModel::class)
    abstract fun bindCropAvatarFragmentViewModel(model: CropAvatarFragmentViewModel): ViewModel


}
