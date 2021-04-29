package com.example.libsysmanagement

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.example.corenetworking.localdata.LocalPref
import com.example.libsysmanagement.model.ScanDataMapper
import com.example.libsysmanagement.model.SessionDetails
import com.example.libsysmanagement.session.SessionTrackerService
import com.example.libsysmanagement.session.SessionTrackerService.Companion.FOREGROUND_SERVICE_DATA
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject

@Module
@InstallIn(SingletonComponent::class)
class AppLifeCycleObserver @Inject constructor(
    @ApplicationContext val context: Context,
    private val localPref: LocalPref,
    private val dataMapper: ScanDataMapper
) :
    LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    private fun checkAndStartService() {
        val session = localPref.getSessionInfo().blockingGet()
        if (session.isNotEmpty()) {
            val sessionDetails = dataMapper.getSessionDetails(session)
            startSessionTrackingService(sessionDetails)
        }
    }

    private fun startSessionTrackingService(sessionDetails: SessionDetails) {
        val intent = Intent(context, SessionTrackerService::class.java).apply {
            action = SessionTrackerService.ACTION_START_FOREGROUND_SERVICE
            putExtra(FOREGROUND_SERVICE_DATA, sessionDetails.startTime)
        }
        ContextCompat.startForegroundService(context, intent)
    }
}
