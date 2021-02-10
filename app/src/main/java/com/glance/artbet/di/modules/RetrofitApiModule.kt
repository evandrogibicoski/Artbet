package com.glance.artbet.di.modules

import android.app.Application
import android.util.Log
import com.glance.artbet.BuildConfig
import com.glance.artbet.data.local_storage.LocalStorageRepository
import com.glance.artbet.domain.repository.user_api.UserApiInterface
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import okio.Buffer
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class RetrofitApiModule(private val localStorageRepository: LocalStorageRepository) {
    companion object {
        private const val CONNECTION_TIMEOUT_SEC = 30L
        private const val CACHE_SIZE = 10_000_000L
        private const val CACHE_FILE_NAME = "http_cache"
    }

    @Provides
    @Singleton
    fun provideHttpCache(application: Application): Cache {
        val cacheSize = 10L * 1024 * 1024
        return Cache(application.cacheDir, cacheSize)
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        val gsonBuilder = GsonBuilder()
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        return gsonBuilder.create()
    }


    @Provides
    @Singleton
    fun provideOkhttpClient(cache: Cache): OkHttpClient {
        return OkHttpClient.Builder().apply {
            cache(cache)

            /*val cookieManager = CookieManager()
            cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL)
            cookieJar(JavaNetCookieJar(cookieManager))*/

            if (BuildConfig.DEBUG) {
                val httpLoggingInterceptor = HttpLoggingInterceptor()
                httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
                addInterceptor(httpLoggingInterceptor)
            }

            connectTimeout(CONNECTION_TIMEOUT_SEC, TimeUnit.SECONDS)
            readTimeout(CONNECTION_TIMEOUT_SEC, TimeUnit.SECONDS)
            writeTimeout(CONNECTION_TIMEOUT_SEC, TimeUnit.SECONDS)
            retryOnConnectionFailure(true)

            addInterceptor { chain ->
                val request = chain.request()

                val token: String = localStorageRepository.getBearerToken().blockingGet()
                Log.d("Token for ${request.url()}", token)
                val change = request.newBuilder()
                    .header("Content-Type", "application/json")
                    .header("Authorization", token)

                val response = chain.proceed(change.build())

                response
            }
        }.build()
    }

    private fun bodyToString(request: RequestBody?): String {
        try {
            val buffer = Buffer()
            if (request != null)
                request.writeTo(buffer)
            else
                return ""
            return buffer.readUtf8()
        } catch (e: IOException) {
            return "did not work"
        }
    }

    @Provides
    @Singleton
    fun provideRetrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.SERVER_URL)
            .client(okHttpClient)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    fun provideUserApiInterface(retrofit: Retrofit): UserApiInterface =
        retrofit.create(UserApiInterface::class.java)

}