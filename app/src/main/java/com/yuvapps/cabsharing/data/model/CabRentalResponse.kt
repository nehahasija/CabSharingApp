package com.yuvapps.cabsharing.data.model

import com.google.gson.annotations.SerializedName

data class CabRentalResponse(
    @SerializedName("carId")
    val carId: Int = 0,
    @SerializedName("reservationId")
    val reservationId: Int = 0,
    @SerializedName("cost")
    val cost:Int=0,
    @SerializedName("drivenDistance")
    val drivenDistance:Int=0,
    @SerializedName("licencePlate")
    val licencePlate:String="",
    @SerializedName("startAddress")
    val startAddress:String="",
    @SerializedName("userId")
    val userId:Int=0,
    @SerializedName("isParkModeEnabled")
    val isParkModeEnabled:Boolean=false,
    @SerializedName("damageDescription")
    val damageDescription:String="",
    @SerializedName("fuelCardPin")
    val fuelCardPin:String="",
    @SerializedName("endTime")
    val endTime:Long=0L,
    @SerializedName("startTime")
    val startTime:Long=0L
)
