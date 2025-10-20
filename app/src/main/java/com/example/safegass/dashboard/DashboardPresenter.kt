package com.example.safegass.dashboard

import android.net.Uri

class DashboardPresenter(
    private var view: DashboardContract.View?,
    private val repository: DashboardContract.Repository
) : DashboardContract.Presenter {

    override fun loadDashboardData() {
        repository.getDashboardData { data, error ->
            if (error != null) {
                view?.showError(error.message ?: "Failed to load data")
                return@getDashboardData
            }

            data?.let {
                view?.showPPM(it.ppm)
                view?.showStatus(it.status)
                view?.showLocation(it.location)
                view?.showImage(it.imageUrl)
                view?.showLastUpdated(it.lastUpdated)
                view?.showOverview(it.activeAlerts, it.onlineDevices, it.avgPPM, it.peakPPM)
            }
        }
    }

    override fun saveLocation(location: String) {
        if (location.isBlank()) {
            view?.showToast("Please enter a location.")
            return
        }

        repository.saveLocation(location) { success ->
            if (success) {
                view?.showToast("Location saved successfully.")
                view?.showLocation(location)
            } else {
                view?.showError("Failed to save location.")
            }
        }
    }

    override fun uploadImage(imageUri: Uri) {
        repository.uploadImage(imageUri) { url ->
            if (url != null) {
                view?.showImage(url)
                view?.showToast("Image uploaded successfully.")
            } else {
                view?.showError("Image upload failed.")
            }
        }
    }

    override fun detach() {
        view = null
    }
}
