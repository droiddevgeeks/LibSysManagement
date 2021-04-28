package com.example.libsysmanagement

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.corenetworking.model.SubmitRequest
import com.example.corenetworking.model.SubmitResponse
import com.example.corenetworking.util.RxScheduler
import com.example.libsysmanagement.model.DataState
import com.example.libsysmanagement.model.ScanDataMapper
import com.example.libsysmanagement.model.SessionDetails
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
import org.junit.jupiter.api.Assertions.*
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

    private val scanDataMapper = mock<ScanDataMapper>()

    private val viewModel by lazy { SessionViewModel(useCase, scheduler, scanDataMapper) }

    private val submitStateObserver by lazy { mock<Observer<DataState<SubmitResponse>>>() }
    private val sessionStateObserver by lazy { mock<Observer<DataState<SessionDetails>>>() }

    @Test
    fun `should submit session successfully`() {
        val submitRequest = SubmitRequest("", 0, 0)
        val submitResponse = SubmitResponse(true)
        val argCaptor = argumentCaptor<DataState<SubmitResponse>>()
        whenever(useCase.endSession()).thenReturn(Single.just(true))
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
        whenever(useCase.endSession()).thenReturn(Single.just(true))
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

    @Test
    fun `Should fetch session details from local preference`() {
        val argCaptor = argumentCaptor<DataState<SessionDetails>>()
        val sessionDetails = TestHelper.getSessionData()

        whenever(useCase.getSessionInfo()).thenReturn(Single.just(TestHelper.SESSION_DETAIL))
        whenever(scanDataMapper.getSessionDetails(TestHelper.SESSION_DETAIL)).thenReturn(
            sessionDetails
        )

        viewModel.sessionLiveData.observeForever(sessionStateObserver)

        viewModel.fetchSessionDetails()


        verify(sessionStateObserver, times(1)).onChanged(argCaptor.capture())

        argCaptor.allValues.forEach { state ->
            when (state) {
                is DataState.Success -> {
                    assertEquals(state.data, sessionDetails)
                    assertEquals(state.data.scanData, sessionDetails.scanData)
                }
            }
        }
    }

    @Test
    fun `Should fetch end session details successfully`() {
        val sessionDetails = TestHelper.getSessionData()

        val pairData = Pair(
            TestHelper.getScanData(),
            sessionDetails.copy(endTime = 0L, totalPrice = 0f, totalTime = 0L)
        )

        val argCaptor = argumentCaptor<DataState<SessionDetails>>()

        whenever(useCase.getSessionInfo()).thenReturn(Single.just(TestHelper.SESSION_DETAIL))
        whenever(
            scanDataMapper.qrWithFinalDetails(TestHelper.SESSION_DETAIL, TestHelper.QRCode)
        ).thenReturn(pairData)


        viewModel.sessionLiveData.observeForever(sessionStateObserver)

        viewModel.fetchEndSessionDetails(TestHelper.QRCode)


        verify(sessionStateObserver, times(1)).onChanged(argCaptor.capture())

        argCaptor.allValues.forEach { state ->
            when (state) {
                is DataState.Success -> {
                    assertEquals(state.data, sessionDetails)
                    assertEquals(state.data.scanData, sessionDetails.scanData)
                }
                is DataState.Error -> assertNull(state.error)
            }
        }
    }

    @Test
    fun `Should not fetch end session details when scan Qr is invalid or different`() {
        val sessionDetails = TestHelper.getSessionData()
        val error = Throwable("Invalid QR Code, Scan Original Code")

        val pairData = Pair(
            TestHelper.getScanData().copy(locationId = "DifferentID"),
            sessionDetails.copy(endTime = 0L, totalPrice = 0f, totalTime = 0L)
        )

        val argCaptor = argumentCaptor<DataState<SessionDetails>>()

        whenever(useCase.getSessionInfo()).thenReturn(Single.just(TestHelper.SESSION_DETAIL))
        whenever(
            scanDataMapper.qrWithFinalDetails(TestHelper.SESSION_DETAIL, TestHelper.QRCode)
        ).thenReturn(pairData)


        viewModel.sessionLiveData.observeForever(sessionStateObserver)

        viewModel.fetchEndSessionDetails(TestHelper.QRCode)


        verify(sessionStateObserver, times(1)).onChanged(argCaptor.capture())

        argCaptor.allValues.forEach { state ->
            when (state) {
                is DataState.Success -> assertNull(state.data)
                is DataState.Error -> assertEquals(state.error.message, error.message)
            }
        }
    }

    @Test
    fun `Should give same session data when local preference is not empty`() {
        val initialSessionData = TestHelper.getSessionData()
        val argCaptor = argumentCaptor<DataState<SessionDetails>>()

        whenever(useCase.getSessionInfo()).thenReturn(Single.just(TestHelper.SESSION_DETAIL))
        whenever(scanDataMapper.getSessionDetails(TestHelper.SESSION_DETAIL))
            .thenReturn(initialSessionData)

        viewModel.sessionLiveData.observeForever(sessionStateObserver)

        viewModel.checkAndStartSession(TestHelper.QRCode)

        verify(sessionStateObserver, times(1)).onChanged(argCaptor.capture())

        argCaptor.allValues.forEach { state ->
            when (state) {
                is DataState.Success -> {
                    assertEquals(state.data, initialSessionData)
                    assertEquals(state.data.scanData, initialSessionData.scanData)
                }
                is DataState.Error -> assertNull(state.error)
            }
        }
    }

    @Test
    fun `Should give new session data when local preference is empty`() {
        val newSessionData = TestHelper.getSessionData()
        val argCaptor = argumentCaptor<DataState<SessionDetails>>()

        whenever(useCase.getSessionInfo()).thenReturn(Single.just(""))
        whenever(scanDataMapper.qrWithInitialDetails(TestHelper.QRCode))
            .thenReturn(TestHelper.SESSION_DETAIL)
        whenever(useCase.startSession(TestHelper.SESSION_DETAIL))
            .thenReturn(Single.just(TestHelper.SESSION_DETAIL))
        whenever(scanDataMapper.getSessionDetails(TestHelper.SESSION_DETAIL))
            .thenReturn(newSessionData)

        viewModel.sessionLiveData.observeForever(sessionStateObserver)

        viewModel.checkAndStartSession(TestHelper.QRCode)

        verify(sessionStateObserver, times(1)).onChanged(argCaptor.capture())

        argCaptor.allValues.forEach { state ->
            when (state) {
                is DataState.Success -> {
                    assertEquals(state.data, newSessionData)
                    assertEquals(state.data.scanData, newSessionData.scanData)
                }
                is DataState.Error -> assertNull(state.error)
            }
        }
    }
}
