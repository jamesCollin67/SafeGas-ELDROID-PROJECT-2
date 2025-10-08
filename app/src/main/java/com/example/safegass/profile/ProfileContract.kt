package com.example.safegass.profile

interface ProfileContract {
    interface View {
        fun showProfile(profile: UserProfile)
        fun showError(message: String)
    }

    interface Presenter {
        fun loadProfile()
        fun detachView()
    }

    interface Model {
        fun getProfile(): UserProfile?
    }
}
