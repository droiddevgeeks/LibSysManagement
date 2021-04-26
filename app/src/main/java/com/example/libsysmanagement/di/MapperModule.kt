package com.example.libsysmanagement.di

import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class MapperModule {

    companion object {

        @Provides
        fun provideGson() = Gson()
    }
}
