package com.example.corenetworking.di

import com.example.corenetworking.repository.SessionRepository
import com.example.corenetworking.repository.SessionRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepoModule {

    @Binds
    abstract fun provideSessionRepository(sessionRepo: SessionRepositoryImpl): SessionRepository
}