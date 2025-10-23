package com.example.safegass.edit

import com.example.safegass.profile.UserProfile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class EditProfileModel : EditProfileContract.Model {

    private val database = FirebaseDatabase.getInstance()
    private val userId = FirebaseAuth.getInstance().currentUser?.uid

    override fun getProfile(callback: (UserProfile?, String?) -> Unit) {
        if (userId == null) {
            callback(null, "User not logged in.")
            return
        }

        val ref = database.getReference("users/$userId")
        ref.get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                val firstName = snapshot.child("firstName").getValue(String::class.java) ?: ""
                val lastName = snapshot.child("lastName").getValue(String::class.java) ?: ""
                val email = snapshot.child("email").getValue(String::class.java) ?: ""
                val phone = snapshot.child("phone").getValue(String::class.java) ?: ""
                callback(UserProfile(firstName, lastName, email, phone), null)
            } else {
                callback(null, "Profile not found.")
            }
        }.addOnFailureListener {
            callback(null, "Error loading profile: ${it.message}")
        }
    }

    override fun updateProfile(
        firstName: String,
        lastName: String,
        email: String,
        phone: String,
        callback: (Boolean, String?) -> Unit
    ) {
        if (userId == null) {
            callback(false, "User not logged in.")
            return
        }

        val ref = database.getReference("users/$userId")
        val updates = mapOf(
            "firstName" to firstName,
            "lastName" to lastName,
            "email" to email,
            "phone" to phone
        )

        ref.updateChildren(updates).addOnSuccessListener {
            callback(true, null)
        }.addOnFailureListener {
            callback(false, it.message)
        }
    }
}
