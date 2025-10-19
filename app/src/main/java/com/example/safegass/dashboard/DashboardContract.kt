package com.example.safegass.dashboard

import android.net.Uri

interface DashboardContract {

    interface View {
        fun showDashboardData(data: DashboardData)
        fun showError(message: String)
        fun showUploadSuccess(imageUrl: String)
        fun showLoading(isLoading: Boolean)
    }

    interface Presenter {
        fun loadDashboardData()
        fun uploadImage(imageUri: Uri)
        fun saveLocation(location: String, imageUrl: String?)
    }
}
