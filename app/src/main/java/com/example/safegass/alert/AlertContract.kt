package com.example.safegass.alert

interface AlertContract {
    interface View {
        fun showAlerts(alerts: List<Alert>)
        fun showError(message: String)
    }
    interface Presenter {
        fun loadAlerts()
    }
}
