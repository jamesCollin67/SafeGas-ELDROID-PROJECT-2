package com.example.safegass.view

class ViewPagePresenter(
    private val view: ViewPageContract.View,
    private val repository: ViewPageContract.Repository
) : ViewPageContract.Presenter {

    override fun onBackButtonClicked() {
        view.navigateToDashboard()
    }

    override fun loadRealtimeData() {
        repository.listenToSensorData(
            callback = { data ->
                if (data != null) view.showSensorData(data)
                else view.showError("No data found.")
            },
            onError = { error ->
                view.showError(error)
            }
        )
    }
}
