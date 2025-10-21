package com.example.safegass.history

data class HistoryRecord(
    val status: String = "",
    val ppm: Int = 0,
    val timestamp: String = "",
    val location: String = ""
)
