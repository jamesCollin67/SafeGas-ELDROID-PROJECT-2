package com.example.safegass.dashboard

import android.net.Uri
import com.google.firebase.database.DatabaseError

interface DashboardContract {

    interface View {
        fun showImage(imageUrl: String)
        fun showPPM(ppm: Int)
        fun showStatus(status: String)
        fun showLocation(location: String)
        fun showLastUpdated(time: String)
        fun showOverview(activeAlerts: Int, onlineDevices: Int, avgPPM: Int, peakPPM: Int)
        fun showToast(message: String)
        fun showError(error: String)
    }

    interface Presenter {
        fun loadDashboardData()
        fun saveLocation(location: String)
        fun uploadImage(imageUri: Uri)
        fun detach()
    }

    interface Repository {
        fun getDashboardData(callback: (DashboardData?, DatabaseError?) -> Unit)
        fun saveLocation(location: String, callback: (Boolean) -> Unit)
        fun uploadImage(imageUri: Uri, callback: (String?) -> Unit)
    }

    data class DashboardData(
        val ppm: Int = 0,
        val status: String = "Safe",
        val location: String = "",
        val imageUrl: String = "",
        val lastUpdated: String = "",
        val activeAlerts: Int = 0,
        val onlineDevices: Int = 0,
        val avgPPM: Int = 0,
        val peakPPM: Int = 0
    )
}
