package com.example.libsysmanagement.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.example.corenetworking.model.SubmitRequest
import com.example.libsysmanagement.R
import com.example.libsysmanagement.R.string
import com.example.libsysmanagement.databinding.ActivityMainBinding
import com.example.libsysmanagement.extension.gone
import com.example.libsysmanagement.extension.inVisible
import com.example.libsysmanagement.extension.visible
import com.example.libsysmanagement.model.DataState
import com.example.libsysmanagement.model.ScanData
import com.google.gson.Gson
import com.google.gson.JsonParser
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

    private lateinit var scanData: ScanData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        result?.let {
            if (it.contents.isNullOrEmpty().not()) {
                val qrCode = JsonParser.parseString(it.contents).asString
                scanData = Gson().fromJson(qrCode, ScanData::class.java)
                Toast.makeText(this, scanData.toString(), Toast.LENGTH_SHORT).show()
                setScanResult(scanData)
            } else {
                showToast(getString(string.result_not_found))
            }
        } ?: super.onActivityResult(requestCode, resultCode, data)
    }

    fun openScanner(view: View) {
        qrScan.initiateScan()
    }

    fun endSession(view: View) {
        with(binding.sessionDetails) {
            endSessionGroup.visible()
            val endTime = Date(System.currentTimeMillis())
            tvEndTimeValue.text = endTime.toString()
        }
    }

    fun payAndSubmit(view: View) {
        val submitRequest = SubmitRequest(scanData.locationId, 2, System.currentTimeMillis())
        viewModel.submitSession(submitRequest)
        observeSubmitSession()
    }

    private fun observeSubmitSession() {
        viewModel.sessionSubmitLiveData.observe(this, { apiState ->
            when (apiState) {
                is DataState.Loading -> handleLoading(apiState.isLoading)
                is DataState.Error -> showToast(apiState.error.message
                    ?: getString(string.something_went_wrong))
                is DataState.Success -> {
                    showToast(getString(string.submitted) + " " + apiState.data.success)
                    resetScan()
                }
            }
        })
    }

    private fun setScanResult(scanData: ScanData) {
        with(binding) {
            btnScan.inVisible()
            btnEndSession.visible()
        }
        with(binding.sessionDetails) {
            sessionDetailLayout.visible()
            tvLocationValue.text = scanData.locationId
            tvLocationDetailValue.text = scanData.locationDetails
            tvPriceValue.text = scanData.pricePerMin
            val date = Date(System.currentTimeMillis())
            tvStartTimeValue.text = date.toString()
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
            btnScan.visible()
            btnEndSession.gone()
            sessionDetails.sessionDetailLayout.gone()
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
