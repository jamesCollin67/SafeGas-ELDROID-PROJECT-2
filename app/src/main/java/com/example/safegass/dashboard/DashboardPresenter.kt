package com.example.safegass.dashboard

import android.os.Handler
import android.os.Looper

class DashboardPresenter(private val view: DashboardContract.View) : DashboardContract.Presenter {

    override fun loadDashboardData() {
        view.showLoading(true)
        Handler(Looper.getMainLooper()).postDelayed({
            val ppm = "285 ppm"
            val status = "Safe"
            val location = "Kitchen Sensor, Living Room"
            val activeAlerts = 0
            val onlineDevices = 3
            val avgPpm = "310 ppm"
            val peakPpm = "640 ppm"
            val lastUpdated = "Last updated 10:30 AM"

            view.showLoading(false)
            view.showPPM(ppm, status, location)
            view.showOverview(activeAlerts, onlineDevices, avgPpm, peakPpm)
            view.showLastUpdated(lastUpdated)
        }, 1200)
    }

    override fun onViewDetailsClicked() {
        view.showDetailsPage()   // ✅ Navigate to ViewPageActivity
    }

    override fun onMuteAlarmClicked() {
        view.showError("Alarm muted temporarily.")
    }

    override fun onCallEmergencyClicked() {
        view.showError("Calling emergency services...")
    }
}
