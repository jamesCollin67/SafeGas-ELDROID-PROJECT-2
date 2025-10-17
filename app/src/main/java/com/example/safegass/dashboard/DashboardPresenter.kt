package com.example.safegass.dashboard

class DashboardPresenter(
    private var view: DashboardContract.View?,
    private val repository: DashboardContract.Repository
) : DashboardContract.Presenter {

    private val repoCallback = object : DashboardContract.RepositoryCallback {
        override fun onData(data: DashboardData) {
            view?.showLoading(false)
            view?.showPPM(data.ppm)
            view?.showStatus(data.status)
            view?.showLocation(data.location)
            view?.showLastUpdated(data.lastUpdated)
            view?.showOverview(
                data.activeAlerts,
                data.onlineDevices,
                data.avgPpm,
                data.peakPpm
            )
        }

        override fun onError(message: String) {
            view?.showLoading(false)
            view?.showError(message)
        }
    }

    override fun start() {
        view?.showLoading(true)
        repository.addListener(repoCallback)
    }

    override fun stop() {
        repository.removeListener()
        // 🔴 keep view alive until destroy, not stop
        // view = null
    }
}
