package com.example.safegass.dashboard

interface DashboardContract {

    interface View {
        fun showLoading(isLoading: Boolean)
        fun showPPM(ppm: String)
        fun showStatus(status: String)
        fun showLocation(location: String)
        fun showLastUpdated(time: String)
        fun showOverview(activeAlerts: Int, onlineDevices: Int, avgPpm: String, peakPpm: String)
        fun showError(message: String)
    }

    interface Presenter {
        fun start()
        fun stop()
    }

    interface Repository {
        fun addListener(callback: RepositoryCallback)
        fun removeListener()
    }

    interface RepositoryCallback {
        fun onData(data: DashboardData)
        fun onError(message: String)
    }
}
