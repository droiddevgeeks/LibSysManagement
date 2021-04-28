package com.example.libsysmanagement

import com.example.corenetworking.model.SubmitRequest
import com.example.corenetworking.model.SubmitResponse
import com.example.corenetworking.repository.SessionRepository
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Session UseCase Test Cases")
class SessionUseCaseTest {

    private val repo by lazy { mock<SessionRepository>() }

    @Test
    fun `should submit session successfully`() {
        val submitRequest = SubmitRequest("", 0, 0)
        val submitResponse = SubmitResponse(true)
        whenever(repo.submitSession(submitRequest)).thenReturn(Single.just(submitResponse))

        repo.submitSession(submitRequest)
            .test()
            .assertSubscribed()
            .assertComplete()
            .assertNoErrors()
            .assertValue(submitResponse)

        verify(repo).submitSession(submitRequest)
        verifyNoMoreInteractions(repo)
    }

    @Test
    fun `Should not submit session successfully`() {
        val submitRequest = SubmitRequest("", 0, 0)
        val errorResponse = Throwable("Some Error")
        whenever(repo.submitSession(submitRequest)).thenReturn(Single.error(errorResponse))

        repo.submitSession(submitRequest)
            .test()
            .assertSubscribed()
            .assertNotComplete()
            .assertNoValues()
            .assertError(errorResponse)

        verify(repo).submitSession(submitRequest)
        verifyNoMoreInteractions(repo)
    }

    @Test
    fun `Should start session by storing scan data in local preference`() {
        val scanData = "Scan Data"
        whenever(repo.startSession(scanData)).thenReturn(Single.create { it.onSuccess(scanData) })

        repo.startSession(scanData)
            .test()
            .assertSubscribed()
            .assertComplete()
            .assertNoErrors()
            .assertValue(scanData)
    }

    @Test
    fun `Should end session by clearing scan data in local preference`() {
        whenever(repo.endSession()).thenReturn(Single.create { it.onSuccess(true) })

        repo.endSession()
            .test()
            .assertSubscribed()
            .assertComplete()
            .assertNoErrors()
            .assertValue(true)
    }

    @Test
    fun `Should get session details from local preference`() {
        val sessionData = "Session Data"
        whenever(repo.getSessionInfo()).thenReturn(Single.create { it.onSuccess(sessionData) })

        repo.getSessionInfo()
            .test()
            .assertSubscribed()
            .assertComplete()
            .assertNoErrors()
            .assertValue(sessionData)
    }
}
