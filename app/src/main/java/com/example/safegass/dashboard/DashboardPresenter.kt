package com.example.safegass.dashboard

import android.net.Uri

class DashboardPresenter(
    private val view: DashboardContract.View,
    private val repository: DashboardRepository
) : DashboardContract.Presenter {

    override fun loadDashboardData() {
        view.showLoading(true)
        repository.fetchDashboardData { data ->
            view.showLoading(false)
            if (data != null) {
                view.showDashboardData(data)
            } else {
                view.showError("Failed to fetch data")
            }
        }
    }

    override fun uploadImage(imageUri: Uri) {
        view.showLoading(true)
        repository.uploadImage(imageUri) { url ->
            view.showLoading(false)
            if (url != null) {
                view.showUploadSuccess(url)
            } else {
                view.showError("Image upload failed")
            }
        }
    }

    override fun saveLocation(location: String, imageUrl: String?) {
        repository.saveLocation(location, imageUrl)
    }
}
