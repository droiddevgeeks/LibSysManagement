package com.example.libsysmanagement.model

import com.google.gson.annotations.SerializedName

/**
 * {
 * location_id:ButterKnifeLib-1234,
 * location_details:ButterKnife Lib, 80 Feet Rd,Koramangala 1A Block, Bangalore,
 * price_per_min:5.50
 * }
 */
data class ScanData(
    @SerializedName("location_id") val locationId: String,
    @SerializedName("location_details") val locationDetails: String,
    @SerializedName("price_per_min") val pricePerMin: Float,
)

data class SessionDetails(
    val scanData: ScanData,
    val startTime: Long = 0L,
    val endTime: Long = 0L,
    val totalPrice: Float = 0.0f,
    val totalTime: Long = 0L
)

sealed class DataState<out T> {
    data class Loading(val isLoading: Boolean) : DataState<Nothing>()
    data class Error(val error: Throwable) : DataState<Nothing>()
    data class Success<T>(val data: T) : DataState<T>()
}
