package com.example.safegass.profile

data class UserProfile(
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val phone: String = "",
    val imageUrl: String? = null
)