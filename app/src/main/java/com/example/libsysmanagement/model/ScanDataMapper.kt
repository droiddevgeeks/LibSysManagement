package com.example.libsysmanagement.model

import com.google.gson.Gson
import com.google.gson.JsonParser
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ScanDataMapper @Inject constructor(private val gson: Gson) {

    fun qrWithInitialDetails(qrCode: String): String {
        return gson.toJson(
            SessionDetails(
                scanData = parseQrCode(qrCode),
                startTime = System.currentTimeMillis()
            )
        )
    }

    fun qrWithFinalDetails(
        initialSessionDetails: String,
        currentQRCode: String
    ): Pair<ScanData, SessionDetails> {
        val initialScanData = getSessionDetails(initialSessionDetails)
        val endTime = System.currentTimeMillis()
        val totalTime = getTimeDiff(initialScanData.startTime, endTime)
        return Pair(
            parseQrCode(currentQRCode),
            initialScanData.copy(
                endTime = endTime,
                totalPrice = totalTime.times(initialScanData.scanData.pricePerMin),
                totalTime = totalTime
            )
        )
    }

    fun getSessionDetails(initialSessionDetails: String): SessionDetails {
        return gson.fromJson(initialSessionDetails, SessionDetails::class.java)
    }

    private fun parseQrCode(qrCode: String): ScanData {
        val formattedQR = JsonParser.parseString(qrCode).asString
        return gson.fromJson(formattedQR, ScanData::class.java)
    }

    private fun getTimeDiff(startTime: Long, endTime: Long): Long =
        TimeUnit.MILLISECONDS.toMinutes(endTime.minus(startTime))
}
