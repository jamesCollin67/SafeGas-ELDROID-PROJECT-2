package com.example.safegass.profile

class ProfilePresenter(
    private var view: ProfileContract.View?,
    private val model: ProfileContract.Model
) : ProfileContract.Presenter {

    override fun loadProfile() {
        val profile = model.getProfile()
        if (profile != null) {
            view?.showProfile(profile)
        } else {
            view?.showError("Failed to load profile.")
        }
    }

    override fun detachView() {
        view = null
    }
}
