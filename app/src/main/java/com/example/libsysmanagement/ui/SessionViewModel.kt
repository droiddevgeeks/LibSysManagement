package com.example.libsysmanagement.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.corenetworking.model.SubmitRequest
import com.example.corenetworking.model.SubmitResponse
import com.example.corenetworking.util.RxScheduler
import com.example.libsysmanagement.model.DataState
import com.example.libsysmanagement.model.ScanDataMapper
import com.example.libsysmanagement.model.SessionDetails
import com.example.libsysmanagement.session.SessionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

@HiltViewModel
class SessionViewModel @Inject constructor(
    private val useCase: SessionUseCase,
    private val scheduler: RxScheduler,
    private var dataMapper: ScanDataMapper
) : ViewModel() {

    private val _sessionSubmitLiveData by lazy { MutableLiveData<DataState<SubmitResponse>>() }
    val sessionSubmitLiveData: LiveData<DataState<SubmitResponse>> by lazy { _sessionSubmitLiveData }

    private val _sessionLiveData by lazy { MutableLiveData<DataState<SessionDetails>>() }
    val sessionLiveData: LiveData<DataState<SessionDetails>> by lazy { _sessionLiveData }

    private val disposable by lazy { CompositeDisposable() }

    fun submitSession(body: SubmitRequest) {
        val dispose = useCase.endSession()
            .flatMap { useCase.submitSession(body) }
            .subscribeOn(scheduler.io)
            .observeOn(scheduler.main)
            .doOnSubscribe { _sessionSubmitLiveData.value = DataState.Loading(true) }
            .doOnEvent { _, _ -> _sessionSubmitLiveData.value = DataState.Loading(false) }
            .doOnError { _sessionSubmitLiveData.value = DataState.Loading(false) }
            .subscribe(
                { _sessionSubmitLiveData.value = DataState.Success(it) },
                { _sessionSubmitLiveData.value = DataState.Error(it) }
            )
        disposable.add(dispose)
    }

    fun checkAndStartSession(scanData: String) {
        val dispose = useCase.getSessionInfo()
            .flatMap {
                if (it.isEmpty()) useCase.startSession(dataMapper.qrWithInitialDetails(scanData))
                else Single.just(it)
            }
            .map { dataMapper.getSessionDetails(it) }
            .subscribeOn(scheduler.io)
            .observeOn(scheduler.main)
            .subscribe(
                { _sessionLiveData.value = DataState.Success(it) },
                { _sessionLiveData.value = DataState.Error(it) }
            )
        disposable.add(dispose)
    }

    fun fetchEndSessionDetails(scanData: String) {
        val dispose = useCase.getSessionInfo()
            .map { dataMapper.qrWithFinalDetails(it, scanData) }
            .flatMap {
                if (it.first.locationId == it.second.scanData.locationId) Single.just(it.second)
                else Single.error(Throwable("Invalid QR Code, Scan Original Code"))
            }
            .subscribeOn(scheduler.io)
            .observeOn(scheduler.main)
            .subscribe(
                { _sessionLiveData.value = DataState.Success(it) },
                { _sessionLiveData.value = DataState.Error(it) }
            )
        disposable.add(dispose)
    }

    fun fetchSessionDetails() {
        val dispose = useCase.getSessionInfo()
            .subscribeOn(scheduler.io)
            .map { dataMapper.getSessionDetails(it) }
            .observeOn(scheduler.main)
            .subscribe({ _sessionLiveData.value = DataState.Success(it) }, {})
        disposable.add(dispose)
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}
