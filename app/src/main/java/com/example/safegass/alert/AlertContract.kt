package com.example.safegass.alert

interface AlertContract {

    interface View {
        fun showAlerts(alerts: List<Alert>)
        fun showError(message: String)
        fun showLoading()
        fun hideLoading()
    }

    interface Presenter {
        fun attachView(view: View)
        fun detachView()
        fun loadAlerts()
        fun muteAlarm(alertId: Int)
        fun callEmergency(alertId: Int)
    }
}
