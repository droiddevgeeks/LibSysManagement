package com.example.libsysmanagement.session

import com.example.corenetworking.model.SubmitRequest
import com.example.corenetworking.model.SubmitResponse
import com.example.corenetworking.repository.SessionRepository
import io.reactivex.Single
import javax.inject.Inject

interface SessionUseCase {
    fun submitSession(body: SubmitRequest): Single<SubmitResponse>
}

class SessionUseCaseImpl @Inject constructor(private val repo: SessionRepository) : SessionUseCase {

    override fun submitSession(body: SubmitRequest) = repo.submitSession(body)
}
