package com.example.safegass.edit

import com.example.safegass.profile.UserProfile

interface EditProfileContract {
    interface View {
        fun showProfile(profile: UserProfile)
        fun showSuccess(message: String)
        fun showError(message: String)
    }

    interface Presenter {
        fun loadProfile()
        fun saveProfileChanges(fullName: String, email: String, phone: String)
        fun detachView()
    }

    interface Model {
        fun getProfile(callback: (UserProfile?, String?) -> Unit)
        fun updateProfile(firstName: String, lastName: String, email: String, phone: String, callback: (Boolean, String?) -> Unit)
    }
}
