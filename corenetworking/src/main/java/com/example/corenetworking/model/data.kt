package com.example.corenetworking.model

/*
{
    location_id: ButterKnifeLib-1234, //string
    time_spent: 60 //in minutes //int
    end_time: 1599467709991 //timestamp //long
}
*/
data class SubmitRequest(
    val location_id: String,
    val time_spent: Int,
    val end_time: Long
)

data class SubmitResponse(
    val success: Boolean
)