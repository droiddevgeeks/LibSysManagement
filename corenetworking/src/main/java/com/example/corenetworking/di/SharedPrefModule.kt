package com.example.corenetworking.di

import android.content.Context
import android.content.SharedPreferences
import com.example.corenetworking.localdata.LocalPref
import com.example.corenetworking.localdata.LocalPrefImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class SharedPrefModule {

    companion object {

        private const val PREF_NAME = "session_pref"

        @Provides
        fun provideSharedPref(@ApplicationContext context: Context): SharedPreferences {
            return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        }
    }

    @Binds
    abstract fun provideLocalPref(pref: LocalPrefImpl): LocalPref
}
