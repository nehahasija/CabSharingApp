package com.yuvapps.cabsharing.data.api

import com.yuvapps.cabsharing.data.model.CabRentalResponse
import com.yuvapps.cabsharing.data.model.CabResponse
import com.yuvapps.cabsharing.utils.Constants
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Body
import retrofit2.http.Url

interface ApiService {
    @GET(Constants.ALL_CABS_URL)
   suspend fun getCabs(): Response<List<CabResponse>>

   @GET("cars/{carId}")
   suspend fun getCab(@Path("carId") id:Int): Response<CabResponse>

   @Headers("Authorization: Bearer df7c313b47b7ef87c64c0f5f5cebd6086bbb0fa")
   @POST
   suspend fun bookQuickRental(@Url url:String,
                               @Body body:CabResponse):Response<CabRentalResponse>
}
