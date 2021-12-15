package com.yuvapps.cabsharing.data.api

import com.yuvapps.cabsharing.data.model.CabResponse
import com.yuvapps.cabsharing.utils.Constants
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val apiService: ApiService) {
    suspend fun getCabs()=apiService.getCabs()
    suspend fun getCab(id: Int) =apiService.getCab(id)
    suspend fun bookRentalCab(cabResponse: CabResponse)=
        apiService.bookQuickRental(Constants.POST_QUICK_RENTAL,cabResponse)
}
