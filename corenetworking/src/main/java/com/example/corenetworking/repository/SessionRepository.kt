package com.example.corenetworking.repository

import com.example.corenetworking.api.SubmitApi
import com.example.corenetworking.localdata.LocalPref
import com.example.corenetworking.model.SubmitRequest
import com.example.corenetworking.model.SubmitResponse
import io.reactivex.Single
import javax.inject.Inject

interface SessionRepository {

    fun submitSession(body: SubmitRequest): Single<SubmitResponse>
    fun startSession(scanData: String): Single<String>
    fun getSessionInfo(): Single<String>
    fun endSession(): Single<Boolean>
}

class SessionRepositoryImpl @Inject constructor(
    private val api: SubmitApi,
    private val localPref: LocalPref) : SessionRepository {

    override fun submitSession(body: SubmitRequest) = api.submitSession(body)

    override fun startSession(scanData: String): Single<String> = localPref.startSession(scanData)

    override fun getSessionInfo(): Single<String> = localPref.getSessionInfo()

    override fun endSession(): Single<Boolean> = localPref.endSession()
}
