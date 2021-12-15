package com.yuvapps.cabsharing.data.api

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Looper
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices.*
import com.yuvapps.cabsharing.data.model.LocationModel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class LocationUpdatesUseCase @Inject constructor(context: Context) {

    private val mContext:Context=context
    private val fusedLocationClient = getFusedLocationProviderClient(context)

    fun fetchUpdates(): Flow<LocationModel> = callbackFlow {
        val locationRequest = LocationRequest.create().apply {
            interval = TimeUnit.SECONDS.toMillis(UPDATE_INTERVAL_SECS)
            fastestInterval = TimeUnit.SECONDS.toMillis(FASTEST_UPDATE_INTERVAL_SECS)
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        val callBack = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                val location = locationResult.lastLocation
                    val userLocation = LocationModel(
                        latitude = location.latitude,
                        longitude = location.longitude,
                    )
                    this@callbackFlow.trySend(userLocation).isSuccess


            }

        }

        if (ActivityCompat.checkSelfPermission(
                mContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                callBack,
                Looper.getMainLooper()
            )
        }
        awaitClose {
            fusedLocationClient.removeLocationUpdates(callBack)
        }
    }

    companion object {
        private const val UPDATE_INTERVAL_SECS = 5000L
        private const val FASTEST_UPDATE_INTERVAL_SECS = 2000L
    }
}