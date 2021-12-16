package com.yuvapps.cabsharing.data.di

import android.content.Context
import com.yuvapps.cabsharing.data.api.LocationUpdatesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)

object RepositoryModule {

    @Provides
    @Singleton
    fun providesLocationUpdateUseCase(@ApplicationContext context: Context)= LocationUpdatesUseCase(
        context = context)
}
