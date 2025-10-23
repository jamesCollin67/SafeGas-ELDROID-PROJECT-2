package com.example.safegass.profile

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class ProfileModel : ProfileContract.Model {

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
                val imageUrl = snapshot.child("imageUrl").getValue(String::class.java)

                callback(
                    UserProfile(firstName, lastName, email, phone, imageUrl),
                    null
                )
            } else {
                callback(null, "Profile not found.")
            }
        }.addOnFailureListener {
            callback(null, "Failed to load profile: ${it.message}")
        }
    }

    override fun updateProfileImage(url: String) {
        if (userId != null) {
            val ref = database.getReference("users/$userId/imageUrl")
            ref.setValue(url)
        }
    }
}
