package com.yuvapps.cabsharing.data.repository
import com.yuvapps.cabsharing.data.api.LocationUpdatesUseCase
import com.yuvapps.cabsharing.data.api.RemoteDataSource
import com.yuvapps.cabsharing.data.api.base.BaseApiResponse
import com.yuvapps.cabsharing.data.model.CabRentalResponse
import com.yuvapps.cabsharing.data.model.CabResponse
import com.yuvapps.cabsharing.data.model.LocationModel
import com.yuvapps.cabsharing.data.model.NetworkResponse
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

@ActivityRetainedScoped
class CabRepository @Inject constructor(private val remoteDataSource: RemoteDataSource,private val webService:LocationUpdatesUseCase) :
    BaseApiResponse() {

    suspend fun getNearbyCabs(): Flow<NetworkResponse<List<CabResponse>>> {
        return flow<NetworkResponse<List<CabResponse>>> {
            emit(safeApiCall { remoteDataSource.getCabs() })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getCab(id: Int): Flow<NetworkResponse<CabResponse>> {
        return flow<NetworkResponse<CabResponse>> {
            emit(safeApiCall { remoteDataSource.getCab(id) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun bookRentalCab(cabResponse: CabResponse): Flow<NetworkResponse<CabRentalResponse>> {
        return flow<NetworkResponse<CabRentalResponse>> {
            emit(safeApiCall { remoteDataSource.bookRentalCab(cabResponse) })
        }.flowOn(Dispatchers.IO)
    }
    fun getLocation(): Flow<LocationModel> {
        return webService.fetchUpdates()
    }

}
