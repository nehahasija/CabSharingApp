package com.yuvapps.cabsharing.data.repository

import com.yuvapps.cabsharing.data.model.LocationModel
import kotlinx.coroutines.flow.Flow

interface LocationRepository {

    fun getLocation(): Flow<LocationModel>

}