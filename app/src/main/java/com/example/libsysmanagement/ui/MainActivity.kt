package com.example.libsysmanagement.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.example.corenetworking.model.SubmitRequest
import com.example.libsysmanagement.R
import com.example.libsysmanagement.R.string
import com.example.libsysmanagement.databinding.ActivityMainBinding
import com.example.libsysmanagement.extension.gone
import com.example.libsysmanagement.extension.visible
import com.example.libsysmanagement.model.DataState
import com.example.libsysmanagement.model.SessionDetails
import com.example.libsysmanagement.session.SessionViewModel
import com.google.zxing.integration.android.IntentIntegrator
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: SessionViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    private val qrScan: IntentIntegrator by lazy {
        initAndConfigScanner()
    }

    private lateinit var sessionDetailsData: SessionDetails

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        handleInitialState()
    }

    private fun handleInitialState() {
        viewModel.fetchSessionDetails()
        observeSessionResult()
    }

    fun openScanner(view: View) {
        qrScan.initiateScan()
    }

    fun payAndSubmit(view: View) {
        viewModel.submitSession(
            body = SubmitRequest(
                sessionDetailsData.scanData.locationId,
                sessionDetailsData.totalTime.toInt(),
                sessionDetailsData.endTime
            )
        )
        observeSubmitSession()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        result?.let {
            if (it.contents.isNullOrEmpty().not()) handleScanResult(it.contents)
            else showToast(getString(string.result_not_found))
        } ?: super.onActivityResult(requestCode, resultCode, data)
    }

    private fun handleScanResult(qrCode: String) {
        if (binding.btnScan.text == getString(string.scan_now))
            viewModel.checkAndStartSession(qrCode)
        else viewModel.fetchEndSessionDetails(qrCode)
    }

    private fun observeSubmitSession() {
        viewModel.sessionSubmitLiveData.observe(this, { apiState ->
            when (apiState) {
                is DataState.Loading -> handleLoading(apiState.isLoading)
                is DataState.Error -> showToast(
                    apiState.error.message
                        ?: getString(string.something_went_wrong)
                )
                is DataState.Success -> {
                    showToast(getString(string.submitted) + " " + apiState.data.success)
                    resetScan()
                }
            }
        })
    }

    private fun observeSessionResult() {
        viewModel.sessionLiveData.observe(this, { scanState ->
            when (scanState) {
                is DataState.Error -> showToast(
                    scanState.error.message
                        ?: getString(string.something_went_wrong)
                )
                is DataState.Success -> {
                    sessionDetailsData = scanState.data
                    setScanResult()
                }
                else -> showToast(getString(string.something_went_wrong))
            }
        })
    }

    private fun setScanResult() {
        with(binding) {
            btnScan.text = getString(string.end_session)
            timer.visible()
        }
        with(binding.sessionDetails) {
            handleClock()
            sessionDetailLayout.visible()
            tvLocationValue.text = sessionDetailsData.scanData.locationId
            tvLocationDetailValue.text = sessionDetailsData.scanData.locationDetails
            tvPriceValue.text = sessionDetailsData.scanData.pricePerMin.toString()
            tvStartTimeValue.text = Date(sessionDetailsData.startTime).toString()
        }
        if (sessionDetailsData.endTime != 0L) {
            handleClock(true)
            with(binding.sessionDetails) {
                endSessionGroup.visible()
                tvEndTimeValue.text = Date(sessionDetailsData.endTime).toString()
                tvTotalTimeValue.text =
                    getString(string.mins, sessionDetailsData.totalTime.toString())
                tvAmountValue.text = sessionDetailsData.totalPrice.toString()
            }
        }
    }

    private fun handleClock(stopClock: Boolean = false) {
        with(binding) {
            if (stopClock) timer.stop()
            else timer.start()
            timer.format = "Time Spend %s"
            timer.base = SystemClock.elapsedRealtime()
                .plus(sessionDetailsData.startTime)
                .minus(System.currentTimeMillis())
        }
    }

    private fun initAndConfigScanner(): IntentIntegrator {
        return IntentIntegrator(this)
            .apply {
                setOrientationLocked(false)
                setPrompt(getString(R.string.scan_prompt))
                setBeepEnabled(true)
            }
    }

    private fun resetScan() {
        with(binding) {
            btnScan.text = getString(string.scan_now)
            sessionDetails.sessionDetailLayout.gone()
            sessionDetails.endSessionGroup.gone()
            timer.gone()
        }
    }

    private fun handleLoading(isLoading: Boolean) {
        with(binding) {
            if (isLoading) progress.visible()
            else progress.gone()
        }
    }

    private fun showToast(message: Any) {
        Toast.makeText(this, "$message", Toast.LENGTH_SHORT).show()
    }
}
