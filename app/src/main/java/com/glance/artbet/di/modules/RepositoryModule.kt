package com.glance.artbet.di.modules

import android.content.Context
import com.glance.artbet.domain.repository.user_api.UserApiInterface
import com.glance.artbet.domain.repository.user_api.UserApiRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [AppModule::class, RetrofitApiModule::class])
class RepositoryModule {

    @Provides
    @Singleton
    fun provideUserApiRepository(
        context: Context,
        userApiInterface: UserApiInterface
    ): UserApiRepository = UserApiRepository(context, userApiInterface)

}