package com.example.safegass.alert

class AlertModel {

    fun getAlerts(): List<Alert> {
        return listOf(
            Alert(1, "Emergency Alert", "Danger! Gas Leak Detected. Take Action Now!", "Kitchen Sensor", "2 min ago", "Danger"),
            Alert(2, "Warning Alert", "Gas levels rising.", "Living Room Sensor", "5 min ago", "Warning"),
            Alert(3, "Info Alert", "Gas system normal.", "Garage Sensor", "10 min ago", "Info")
        )
    }

    fun muteAlert(alertId: Int) {
        // TODO: Implement mute logic
    }

    fun callEmergency(alertId: Int) {
        // TODO: Implement call emergency logic
    }
}
