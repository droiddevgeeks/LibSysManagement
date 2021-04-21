package com.example.corenetworking.di

import com.example.corenetworking.api.BASE_URL
import com.example.corenetworking.api.SubmitApi
import com.example.corenetworking.util.CommonSchedulerProvider
import com.example.corenetworking.util.RxScheduler
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkModule {
    companion object {
        @Provides
        fun provideRetrofit(): Retrofit {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(OkHttpClient.Builder().build())
                .build()
        }

        @Provides
        fun provideApiInterface(retrofit: Retrofit): SubmitApi {
            return retrofit.create(SubmitApi::class.java)
        }
    }

    @Binds
    abstract fun provideRxScheduler(scheduler: CommonSchedulerProvider): RxScheduler
}