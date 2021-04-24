package com.example.libsysmanagement.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.corenetworking.model.SubmitRequest
import com.example.corenetworking.model.SubmitResponse
import com.example.corenetworking.util.RxScheduler
import com.example.libsysmanagement.model.DataState
import com.example.libsysmanagement.session.SessionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

@HiltViewModel
class SessionViewModel @Inject constructor(
    private val useCase: SessionUseCase,
    private val scheduler: RxScheduler
) : ViewModel() {

    private val _sessionSubmitLiveData by lazy { MutableLiveData<DataState<SubmitResponse>>() }
    val sessionSubmitLiveData: LiveData<DataState<SubmitResponse>> by lazy { _sessionSubmitLiveData }

    private val disposable by lazy { CompositeDisposable() }


    fun submitSession(body: SubmitRequest) {
        val dispose = useCase.submitSession(body)
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
}
