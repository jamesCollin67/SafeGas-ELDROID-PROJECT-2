package com.example.safegass.connected

data class Device(
    var serial: String = "",
    var location: String = "",
    var status: String = "Offline",
    var lastUpdated: String = ""
)
