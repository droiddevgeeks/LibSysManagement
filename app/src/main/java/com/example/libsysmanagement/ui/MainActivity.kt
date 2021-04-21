package com.example.libsysmanagement.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.libsysmanagement.R
import com.example.libsysmanagement.databinding.ActivityMainBinding
import com.example.libsysmanagement.extension.inVisible
import com.example.libsysmanagement.extension.visible
import com.example.libsysmanagement.model.ScanData
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.google.zxing.integration.android.IntentIntegrator
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val qrScan: IntentIntegrator by lazy {
        initAndConfigScanner()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        result?.let {
            if (it.contents.isNullOrEmpty().not()) {
                val data = JsonParser.parseString(it.contents).asString
                val scanData = Gson().fromJson(data, ScanData::class.java)
                Toast.makeText(this, scanData.toString(), Toast.LENGTH_SHORT).show()
                setScanResult(scanData)
            } else {
                Toast.makeText(this, getString(R.string.result_not_found), Toast.LENGTH_LONG).show()
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

    private fun setScanResult(scanData: ScanData) {
        with(binding) {
            btnScan.inVisible()
            btnEndSession.visible()
        }
        with(binding.sessionDetails) {
            sessionDetailLayout.visibility = View.VISIBLE
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
}