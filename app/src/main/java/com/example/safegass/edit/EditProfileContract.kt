package com.example.safegass.edit

import com.example.safegass.profile.UserProfile

interface EditProfileContract {

    interface View {
        fun showProfile(profile: UserProfile)
        fun showSaveSuccess()
        fun showError(message: String)
    }

    interface Presenter {
        fun loadProfile()
        fun saveProfile(updatedProfile: UserProfile)
        fun detachView()
    }

    interface Model {
        fun getProfile(): UserProfile?
        fun updateProfile(profile: UserProfile): Boolean
    }
}
