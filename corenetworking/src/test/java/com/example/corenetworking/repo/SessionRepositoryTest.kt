package com.example.corenetworking.repo

import com.example.corenetworking.api.SubmitApi
import com.example.corenetworking.model.SubmitRequest
import com.example.corenetworking.model.SubmitResponse
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import retrofit2.HttpException
import retrofit2.Response

@DisplayName("Session Repository Test Cases" )
class SessionRepositoryTest {

    private val endpoint by lazy { mock<SubmitApi>() }

    @Test
    fun `should return success as true when calling submit payment api`() {
        val expectedResponse = SubmitResponse(true)
        val body = getRequestBody()
        whenever(endpoint.submitSession(body))
            .thenReturn(Single.create { it.onSuccess(expectedResponse) })
        endpoint.submitSession(body)
            .test()
            .assertSubscribed()
            .assertComplete()
            .assertNoErrors()
            .assertValue(expectedResponse)
    }

    @Test
    fun `should return success as false when calling submit payment api`() {
        val expectedResponse = SubmitResponse(false)
        val body = getRequestBody()
        whenever(endpoint.submitSession(body))
            .thenReturn(Single.create { it.onSuccess(expectedResponse) })
        endpoint.submitSession(body)
            .test()
            .assertSubscribed()
            .assertComplete()
            .assertNoErrors()
            .assertValue(expectedResponse)
    }

    @Test
    fun `should return error when calling submit payment api`() {

        val errorBody = Response.error<SubmitResponse>(
            400,
            "{\"key\":[\"something went wrong\"]}".toResponseBody("application/json".toMediaTypeOrNull())
        )
        val exception = HttpException(errorBody)
        val requestBody = getRequestBody()
        whenever(endpoint.submitSession(requestBody)).thenReturn(Single.error(exception))
        endpoint.submitSession(getRequestBody())
            .test()
            .assertSubscribed()
            .assertNoValues()
            .assertError { it is HttpException }

        verify(endpoint).submitSession(requestBody)
    }

    private fun getRequestBody(): SubmitRequest {
        return SubmitRequest("1", 1, 2)
    }
}