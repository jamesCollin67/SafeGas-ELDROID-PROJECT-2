package com.example.safegass.alert

class AlertPresenter(private val model: AlertModel) : AlertContract.Presenter {

    private var view: AlertContract.View? = null

    override fun attachView(view: AlertContract.View) {
        this.view = view
    }

    override fun detachView() {
        view = null
    }

    override fun loadAlerts() {
        view?.showLoading()
        try {
            val alerts = model.getAlerts()
            view?.showAlerts(alerts)
        } catch (e: Exception) {
            view?.showError(e.message ?: "Unknown error")
        } finally {
            view?.hideLoading()
        }
    }

    override fun muteAlarm(alertId: Int) {
        model.muteAlert(alertId)
    }

    override fun callEmergency(alertId: Int) {
        model.callEmergency(alertId)
    }
}
