package com.example.safegass.view

data class DeviceDetails(
    val status: String = "",
    val online: String = "",
    val batteryLevel: String = "",
    val upTimeDays: String = "",
    val lastCalibration: String = "",
    val wifiSignal: String = "",
    val deviceId: String = ""
)

data class LeakHistoryRecord(
    val ppm: Int = 0,
    val timestamp: String = "",
    val status: String = "",
    val location: String = ""
)
