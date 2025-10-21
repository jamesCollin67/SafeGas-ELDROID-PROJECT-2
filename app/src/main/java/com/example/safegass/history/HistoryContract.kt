package com.example.safegass.history

interface HistoryContract {

    interface View {
        fun showHistory(records: List<HistoryRecord>)
        fun showError(message: String)
        fun navigateToDashboard()
        fun navigateToAlert()
        fun navigateToSettings()
    }

    interface Presenter {
        fun loadHistory()
        fun applyFilters(from: String, to: String, location: String)
        fun resetFilters()
        fun onDashboardClicked()
        fun onAlertClicked()
        fun onSettingsClicked()
    }
}
