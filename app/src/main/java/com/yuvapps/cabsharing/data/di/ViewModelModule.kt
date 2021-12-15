package com.yuvapps.cabsharing.data.di

import com.yuvapps.cabsharing.data.repository.LocationRepository
import com.yuvapps.cabsharing.data.repository.LocationRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Named


@Module
@InstallIn(ViewModelComponent::class)
abstract class ViewModelModule {

    @Binds
    @ViewModelScoped
    abstract fun bindRepository(repo: LocationRepositoryImpl): LocationRepository
}