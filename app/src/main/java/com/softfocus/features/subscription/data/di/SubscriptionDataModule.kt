package com.softfocus.features.subscription.data.di

import android.content.Context
import com.softfocus.core.data.local.UserSession
import com.softfocus.core.networking.ApiConstants
import com.softfocus.features.subscription.data.remote.SubscriptionService
import com.softfocus.features.subscription.data.repositories.SubscriptionRepositoryImpl
import com.softfocus.features.subscription.domain.repositories.SubscriptionRepository
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object SubscriptionDataModule {

    fun getSubscriptionRepository(context: Context): SubscriptionRepository {
        return SubscriptionRepositoryImpl(getSubscriptionService(context))
    }

    private fun getSubscriptionService(context: Context): SubscriptionService {
        return getRetrofit(context).create(SubscriptionService::class.java)
    }

    private fun getRetrofit(context: Context): Retrofit {
        return Retrofit.Builder()
            .baseUrl(ApiConstants.BASE_URL)
            .client(getOkHttpClient(context))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun getOkHttpClient(context: Context): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val authInterceptor = Interceptor { chain ->
            val userSession = UserSession(context)
            val token = userSession.getUser()?.token

            val requestBuilder = chain.request().newBuilder()
            if (token != null) {
                requestBuilder.addHeader("Authorization", "Bearer $token")
            }

            chain.proceed(requestBuilder.build())
        }

        return OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()
    }
}
