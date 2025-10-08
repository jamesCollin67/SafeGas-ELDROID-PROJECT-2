package com.example.safegass.alert

class AlertPresenter(private val view: AlertContract.View) : AlertContract.Presenter {

    override fun loadAlerts() {
        // Replace with real data source
        val alerts = listOf(
            Alert("Danger", "Gas leak detected", "Kitchen Sensor", "2 min ago"),
            Alert("Warning", "Low battery", "Living Room Sensor", "10 min ago"),
            Alert("Info", "System check passed", "Main Panel", "1 hour ago")
        )
        view.showAlerts(alerts)
    }
}
