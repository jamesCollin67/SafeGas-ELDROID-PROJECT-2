package com.example.safegass.dashboard

interface DashboardContract {

    interface View {
        fun showPPM(ppm: String, status: String, location: String)
        fun showOverview(activeAlerts: Int, onlineDevices: Int, avgPpm: String, peakPpm: String)
        fun showLastUpdated(time: String)
        fun showError(message: String)
        fun showLoading(isLoading: Boolean)
    }

    interface Presenter {
        fun loadDashboardData()
        fun onViewDetailsClicked()
        fun onMuteAlarmClicked()
        fun onCallEmergencyClicked()
    }
}
