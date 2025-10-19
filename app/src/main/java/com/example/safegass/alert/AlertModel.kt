package com.example.safegass.alert

data class Alert(
    val type: String = "",
    val title: String = "",
    val description: String = "",
    val source: String = "",
    val time: Long = 0   // ✅ FIX: change from String to Long
)
