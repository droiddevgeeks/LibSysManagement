package com.example.libsysmanagement

import com.example.libsysmanagement.model.ScanDataMapper
import com.example.libsysmanagement.model.SessionDetails
import com.google.gson.Gson
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.util.concurrent.TimeUnit

@DisplayName("Scan Data Mapper Test Cases")
class ScanDataMapperTest {

    private val gson = Gson()
    private val scanDataMapper: ScanDataMapper = ScanDataMapper(gson)

    @Test
    fun `Should map QR code to Session Detail String`() {
        val expectedSessionData = SessionDetails(
            scanData = TestHelper.getScanData(),
            startTime = System.currentTimeMillis()
        )

        val actualSessionData = gson.fromJson(
            scanDataMapper.qrWithInitialDetails(TestHelper.QRCode),
            SessionDetails::class.java
        )

        assertEquals(expectedSessionData.scanData, actualSessionData.scanData)
        assertEquals(expectedSessionData, actualSessionData)
    }

    @Test
    fun `Should map session detail string to Session Detail object`() {
        val expectedSessionData = TestHelper.getSessionData()

        val actualSessionData = scanDataMapper.getSessionDetails(TestHelper.SESSION_DETAIL)

        assertEquals(expectedSessionData.scanData, actualSessionData.scanData)
        assertEquals(expectedSessionData.startTime, actualSessionData.startTime)
    }

    @Test
    fun `Should map QR code to final Session Detail object`() {
        val scanData = TestHelper.getScanData()
        val initialSessionData = TestHelper.getSessionData()
        val endTime = System.currentTimeMillis()
        val totalTime = TimeUnit.MILLISECONDS.toMinutes(endTime.minus(initialSessionData.startTime))

        val expectedSessionData = Pair(
            scanData, initialSessionData.copy(
                endTime = endTime,
                totalPrice = totalTime.times(scanData.pricePerMin),
                totalTime = totalTime
            )
        )

        val actualSessionData =
            scanDataMapper.qrWithFinalDetails(TestHelper.SESSION_DETAIL, TestHelper.QRCode)

        assertEquals(expectedSessionData.first, actualSessionData.first)
        assertEquals(expectedSessionData.second, actualSessionData.second)
        assertEquals(expectedSessionData, actualSessionData)
    }

}
