package com.example.safegass.profile

class ProfileModel : ProfileContract.Model {

    override fun getProfile(): UserProfile? {
        // Mock data for now — you can later replace this with a database or API call
        return UserProfile(
            name = "Sophia Carter",
            email = "sophia.carter@example.com",
            phone = "(555) 123-4567",
            address = "123 Green St.",
            city = "Cebu City",
            state = "Cebu",
            zipCode = "6000"
        )
    }
}
