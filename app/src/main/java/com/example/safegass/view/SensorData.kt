package com.example.safegass.view

data class SensorData(
    val status: String = "Unknown",
    val online: Boolean = false,
    val batteryLevel: Int = 0,
    val uptimeDays: Int = 0,
    val lastCalibration: String = "",
    val wifiSignal: Int = -100,
    val deviceId: String = ""
)
