package com.example.safegass.history

class HistoryPresenter(private val view: HistoryContract.View) : HistoryContract.Presenter {

    override fun onDashboardClicked() {
        view.navigateToDashboard()
    }

    override fun onAlertClicked() {
        view.navigateToAlert()
    }

    override fun onSettingsClicked() {
        view.navigateToSettings()
    }
}
