package com.example.safegass.history

interface HistoryContract {

    interface View {
        fun navigateToDashboard()
        fun navigateToAlert()
        fun navigateToSettings()
    }

    interface Presenter {
        fun onDashboardClicked()
        fun onAlertClicked()
        fun onSettingsClicked()
    }
}
