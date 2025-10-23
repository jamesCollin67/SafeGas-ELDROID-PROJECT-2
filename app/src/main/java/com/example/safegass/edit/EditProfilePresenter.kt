package com.example.safegass.edit

class EditProfilePresenter(
    private var view: EditProfileContract.View?,
    private val model: EditProfileContract.Model
) : EditProfileContract.Presenter {

    override fun loadProfile() {
        model.getProfile { profile, error ->
            if (profile != null) {
                view?.showProfile(profile)
            } else {
                view?.showError(error ?: "Failed to load profile.")
            }
        }
    }

    override fun saveProfileChanges(fullName: String, email: String, phone: String) {
        if (fullName.isBlank() || email.isBlank()) {
            view?.showError("Please fill out all fields.")
            return
        }

        // Split full name into first + last
        val parts = fullName.trim().split(" ", limit = 2)
        val firstName = parts.getOrNull(0) ?: ""
        val lastName = parts.getOrNull(1) ?: ""

        model.updateProfile(firstName, lastName, email, phone) { success, error ->
            if (success) {
                view?.showSuccess("Profile updated successfully!")
            } else {
                view?.showError(error ?: "Failed to update profile.")
            }
        }
    }

    override fun detachView() {
        view = null
    }
}
