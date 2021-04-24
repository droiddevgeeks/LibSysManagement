package com.example.libsysmanagement.di

import com.example.libsysmanagement.session.SessionUseCase
import com.example.libsysmanagement.session.SessionUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class UseCaseModule {

    @Binds
    abstract fun provideSubmitUseCase(useCase: SessionUseCaseImpl): SessionUseCase
}