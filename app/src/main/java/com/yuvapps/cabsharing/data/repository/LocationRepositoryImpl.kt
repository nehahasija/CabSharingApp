package com.yuvapps.cabsharing.data.repository

import com.yuvapps.cabsharing.data.api.LocationUpdatesUseCase
import com.yuvapps.cabsharing.data.model.LocationModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationRepositoryImpl @Inject constructor(private val webService : LocationUpdatesUseCase) : LocationRepository {
    override fun getLocation(): Flow<LocationModel> {
      return  webService.fetchUpdates()
    }

}