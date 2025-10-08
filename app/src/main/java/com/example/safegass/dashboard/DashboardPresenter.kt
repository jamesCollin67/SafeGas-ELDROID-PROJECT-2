package com.example.safegass.dashboard

class DashboardPresenter(private val view: DashboardContract.View) : DashboardContract.Presenter {

    override fun loadDashboardData() {
        // Simulate data (later this can come from sensors, DB, or API)
        val ppm = "285 ppm"
        val status = "Safe"
        val location = "Kitchen Sensor, Living Room"
        val activeAlerts = 0
        val onlineDevices = 3
        val avgPpm = "310 ppm"
        val peakPpm = "640 ppm"
        val lastUpdated = "Last updated 10:30 AM"

        // Update the view
        view.showPPM(ppm, status, location)
        view.showOverview(activeAlerts, onlineDevices, avgPpm, peakPpm)
        view.showLastUpdated(lastUpdated)
    }
}
