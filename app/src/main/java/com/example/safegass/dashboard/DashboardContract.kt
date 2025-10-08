package com.example.safegass.dashboard

interface DashboardContract {

    interface View {
        fun showPPM(ppm: String, status: String, location: String)
        fun showOverview(activeAlerts: Int, onlineDevices: Int, avgPpm: String, peakPpm: String)
        fun showLastUpdated(time: String)
    }

    interface Presenter {
        fun loadDashboardData()
    }
}
