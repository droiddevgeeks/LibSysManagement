package com.example.libsysmanagement

import com.example.libsysmanagement.model.ScanData
import com.example.libsysmanagement.model.SessionDetails
import com.google.gson.Gson
import com.google.gson.JsonParser

object TestHelper {

    const val QRCode =
        "\"{\\\"location_id\\\":\\\"TimberLib-6789\\\",\\\"location_details\\\":\\\"Timber Lib, 7th block, Koramangala, Bangalore\\\",\\\"price_per_min\\\":4.0}\""

    const val SESSION_DETAIL =
        "{\"endTime\":0,\"scanData\":{\"location_details\":\"Timber Lib, 7th block, Koramangala, Bangalore\",\"location_id\":\"TimberLib-6789\",\"price_per_min\":4.0},\"startTime\":1619550235285,\"totalPrice\":0.0,\"totalTime\":0}"

    fun getScanData(): ScanData {
        val formattedQR = JsonParser.parseString(QRCode).asString
        return Gson().fromJson(formattedQR, ScanData::class.java)
    }

    fun getSessionData(): SessionDetails {
        val sessionData = SESSION_DETAIL
        return Gson().fromJson(sessionData, SessionDetails::class.java)
    }
}
