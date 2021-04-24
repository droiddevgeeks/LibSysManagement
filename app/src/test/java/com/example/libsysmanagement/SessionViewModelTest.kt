package com.example.libsysmanagement

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.corenetworking.model.SubmitRequest
import com.example.corenetworking.model.SubmitResponse
import com.example.corenetworking.util.RxScheduler
import com.example.libsysmanagement.model.DataState
import com.example.libsysmanagement.session.SessionUseCase
import com.example.libsysmanagement.ui.SessionViewModel
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName

@DisplayName("SessionViewModel Test Cases")
class SessionViewModelTest {

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    private val useCase by lazy { mock<SessionUseCase>() }
    private val scheduler = mock<RxScheduler> {
        on { io } doReturn Schedulers.trampoline()
        on { main } doReturn Schedulers.trampoline()
    }

    private val viewModel by lazy { SessionViewModel(useCase, scheduler) }

    private val submitStateObserver by lazy { mock<Observer<DataState<SubmitResponse>>>() }

    @Test
    fun `should submit session successfully`() {
        val submitRequest = SubmitRequest("", 0, 0)
        val submitResponse = SubmitResponse(true)
        val argCaptor = argumentCaptor<DataState<SubmitResponse>>()
        whenever(useCase.submitSession(submitRequest)).thenReturn(Single.just(submitResponse))

        viewModel.sessionSubmitLiveData.observeForever(submitStateObserver)

        viewModel.submitSession(submitRequest)

        /**
         * Loading True, Loading false, Data
         */
        verify(submitStateObserver, times(3)).onChanged(argCaptor.capture())

        argCaptor.allValues.forEach { state ->
            when (state) {
                is DataState.Loading -> {
                    if (state.isLoading) assertTrue(state.isLoading)
                    else assertFalse(state.isLoading)
                }
                is DataState.Error -> assertNull(state.error)
                is DataState.Success -> assertEquals(state.data, submitResponse)
            }
        }
    }

    @Test
    fun `should return error while submitting session`() {
        val submitRequest = SubmitRequest("", 0, 0)
        val submitError = mock<Throwable>()
        val argCaptor = argumentCaptor<DataState<SubmitResponse>>()
        whenever(useCase.submitSession(submitRequest)).thenReturn(Single.error(submitError))

        viewModel.sessionSubmitLiveData.observeForever(submitStateObserver)

        viewModel.submitSession(submitRequest)

        /**
         * Loading True, Loading false, Loading False, State error
         */
        verify(submitStateObserver, times(4)).onChanged(argCaptor.capture())

        argCaptor.allValues.forEach { state ->
            when (state) {
                is DataState.Loading -> {
                    if (state.isLoading) assertTrue(state.isLoading)
                    else assertFalse(state.isLoading)
                }
                is DataState.Error -> assertEquals(state.error, submitError)
                is DataState.Success -> assertNull(state.data)
            }
        }
    }

}
