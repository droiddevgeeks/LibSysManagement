package com.example.corenetworking.api

import com.example.corenetworking.model.SubmitRequest
import com.example.corenetworking.model.SubmitResponse
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST

const val BASE_URL = "https://en478jh796m7w.x.pipedream.net/"

interface SubmitApi {

    @POST("submit-session")
    fun submitSession(@Body request: SubmitRequest): Single<SubmitResponse>
}