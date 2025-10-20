package com.example.safegass.alert

interface AlertContract {
    interface View {
        fun showAlerts(alerts: List<Alert>)
        fun showError(message: String)
        fun showDashboardStatus(alert: Alert)

    }

    interface Presenter {
        fun loadAlerts()
        fun removeListeners() // optional to stop listening when activity destroyed
    }
}
