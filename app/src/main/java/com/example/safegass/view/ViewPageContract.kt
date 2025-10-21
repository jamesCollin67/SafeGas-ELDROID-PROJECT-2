package com.example.safegass.view

interface ViewPageContract {

    interface View {
        fun showDeviceDetails(details: DeviceDetails)
        fun showLeakHistory(historyList: List<LeakHistoryRecord>)
        fun showError(message: String)
    }

    interface Presenter {
        fun loadDeviceDetails()
        fun loadLeakHistory()
    }
}
