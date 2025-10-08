package com.example.safegass.edit

import com.example.safegass.profile.UserProfile

class EditProfilePresenter(
    private var view: EditProfileContract.View?,
    private val model: EditProfileContract.Model
) : EditProfileContract.Presenter {

    override fun loadProfile() {
        val profile = model.getProfile()
        if (profile != null) {
            view?.showProfile(profile)
        } else {
            view?.showError("Failed to load profile.")
        }
    }

    override fun saveProfile(updatedProfile: UserProfile) {
        val success = model.updateProfile(updatedProfile)
        if (success) {
            view?.showSaveSuccess()
        } else {
            view?.showError("Failed to save changes.")
        }
    }

    override fun detachView() {
        view = null
    }
}
