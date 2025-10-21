package com.example.safegass.view

class ViewPagePresenter(private val view: ViewPageContract.View) : ViewPageContract.Presenter {

    private val model = ViewPageModel()

    override fun loadDeviceDetails() {
        model.fetchDeviceDetails(
            callback = { details ->
                if (details != null) view.showDeviceDetails(details)
                else view.showError("Device details unavailable.")
            },
            onError = { message -> view.showError(message) }
        )
    }

    override fun loadLeakHistory() {
        model.fetchLeakHistory(
            callback = { records -> view.showLeakHistory(records) },
            onError = { message -> view.showError(message) }
        )
    }
}
