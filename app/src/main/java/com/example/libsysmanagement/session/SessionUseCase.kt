package com.example.libsysmanagement.session

import com.example.corenetworking.model.SubmitRequest
import com.example.corenetworking.model.SubmitResponse
import com.example.corenetworking.repository.SessionRepository
import io.reactivex.Single
import javax.inject.Inject

interface SessionUseCase {

    fun submitSession(body: SubmitRequest): Single<SubmitResponse>
    fun startSession(scanData: String): Single<String>
    fun getSessionInfo(): Single<String>
    fun endSession(): Single<Boolean>
}

class SessionUseCaseImpl @Inject constructor(private val repo: SessionRepository) : SessionUseCase {

    override fun submitSession(body: SubmitRequest) = repo.submitSession(body)
    override fun startSession(scanData: String) = repo.startSession(scanData)
    override fun getSessionInfo(): Single<String> = repo.getSessionInfo()
    override fun endSession() = repo.endSession()
}
