package com.example.safegass.profile

interface ProfileContract {
    interface View {
        fun showProfile(profile: UserProfile)
        fun showError(message: String)
    }

    interface Presenter {
        fun loadProfile()
        fun saveProfileImageUrl(url: String)
        fun detachView()
    }

    interface Model {
        fun getProfile(callback: (UserProfile?, String?) -> Unit)
        fun updateProfileImage(url: String)
    }
}
