package com.example.safegass.edit

import com.example.safegass.profile.UserProfile

class EditProfileModel : EditProfileContract.Model {

    // Simulate persistent storage (in a real app, replace with SharedPreferences or Database)
    private var currentProfile = UserProfile(
        name = "Sophia Carter",
        email = "sophia.carter@example.com",
        phone = "(555) 123-4567",
        address = "123 Green St.",
        city = "Cebu City",
        state = "Cebu",
        zipCode = "6000"
    )

    override fun getProfile(): UserProfile? {
        return currentProfile
    }

    override fun updateProfile(profile: UserProfile): Boolean {
        currentProfile = profile
        return true // Simulate successful update
    }
}
