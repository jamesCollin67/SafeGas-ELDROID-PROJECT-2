package com.example.safegass.view

interface ViewPageContract {

    interface View {
        fun navigateToDashboard()
        fun showSensorData(data: SensorData)
        fun showError(message: String)
    }

    interface Presenter {
        fun onBackButtonClicked()
        fun loadRealtimeData()
    }

    interface Repository {
        fun listenToSensorData(callback: (SensorData?) -> Unit, onError: (String) -> Unit)
    }
}
