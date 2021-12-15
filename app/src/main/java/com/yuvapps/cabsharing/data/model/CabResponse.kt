package com.yuvapps.cabsharing.data.model

import com.google.gson.annotations.SerializedName

data class CabResponse(
    @SerializedName("carId")
    val carId: Int = 0,
    @SerializedName("title")
    val title: String = "",
    @SerializedName("lat")
    val lat: Double = 0.0,
    @SerializedName("lon")
    val lon: Double = 0.0,
    @SerializedName("licencePlate")
    val licencePlate: String = "",
    @SerializedName("fuelLevel")
    val fuelLevel: Int = 0,
    @SerializedName("vehicleStateId")
    val vehicleStateId: Int = 0,
    @SerializedName("vehicleTypeId")
    val vehicleTypeId: Int = 1,
    @SerializedName("pricingTime")
    val pricingTime: String = "",
    @SerializedName("pricingParking")
    val pricingParking: String = "",
    @SerializedName("reservationState")
    val reservationState: Int = 0,
    @SerializedName("isClean")
    val isClean: Boolean = false,
    @SerializedName("isDamaged")
    val isDamaged: Boolean = false,
    @SerializedName("distance")
    val distance: String = "",
    @SerializedName("address")
    val address: String = "",
    @SerializedName("zipCode")
    val zipCode: String = "",
    @SerializedName("city")
    val city: String = "",
    @SerializedName("locationId")
    val locationId: Int = 0,
    @SerializedName("vehicleTypeImageUrl")
    val vehicleTypeImageUrl:String="",
    @SerializedName("damageDescription")
    val damageDescription:String ="",
    @SerializedName("isActivatedByHardware")
    val isActivatedByHardware:Boolean=false


)
