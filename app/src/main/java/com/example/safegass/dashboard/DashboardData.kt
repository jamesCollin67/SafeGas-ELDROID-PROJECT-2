package com.example.safegass.dashboard

data class DashboardData(
    val ppm: String = "0 ppm",
    val status: String = "Unknown",
    val location: String = "Unknown",
    val imageUrl: String? = null,
    val lastUpdated: String = "",
    val activeAlerts: Int = 0,
    val onlineDevices: Int = 0,
    val avgPpm: String = "0 ppm",
    val peakPpm: String = "0 ppm"
)
