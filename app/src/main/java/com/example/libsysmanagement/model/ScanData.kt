package com.example.libsysmanagement.model

import com.google.gson.annotations.SerializedName

/**
 * {
 * location_id:ButterKnifeLib-1234,
 * location_details:ButterKnife Lib, 80 Feet Rd,Koramangala 1A Block, Bangalore\&quot;,
 * price_per_min:5.50
 * }
 */
data class ScanData(
    @SerializedName("location_id") val locationId: String,
    @SerializedName("location_details") val locationDetails: String,
    @SerializedName("price_per_min") val pricePerMin: String,
)