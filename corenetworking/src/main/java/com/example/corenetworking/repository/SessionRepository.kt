package com.example.corenetworking.repository

import com.example.corenetworking.api.SubmitApi
import com.example.corenetworking.model.SubmitRequest
import com.example.corenetworking.model.SubmitResponse
import io.reactivex.Single
import javax.inject.Inject

interface SessionRepository {
    fun submitSession(body: SubmitRequest): Single<SubmitResponse>
}

class SessionRepositoryImpl @Inject constructor(private val api: SubmitApi) : SessionRepository {

    override fun submitSession(body: SubmitRequest) = api.submitSession(body)
}