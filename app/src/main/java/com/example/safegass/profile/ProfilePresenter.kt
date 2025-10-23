package com.example.safegass.profile

class ProfilePresenter(
    private var view: ProfileContract.View?,
    private val model: ProfileContract.Model
) : ProfileContract.Presenter {

    override fun loadProfile() {
        model.getProfile { profile, error ->
            if (profile != null) {
                view?.showProfile(profile)
            } else {
                view?.showError(error ?: "Failed to load profile.")
            }
        }
    }

    override fun saveProfileImageUrl(url: String) {
        model.updateProfileImage(url)
    }

    override fun detachView() {
        view = null
    }
}