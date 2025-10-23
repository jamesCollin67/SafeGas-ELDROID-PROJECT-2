package com.example.safegass.update

import android.content.Context
import android.widget.Toast
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class UpdatePasswordPresenter(
    private val view: UpdatePasswordContract.View,
    private val context: Context
) : UpdatePasswordContract.Presenter {

    private val auth = FirebaseAuth.getInstance()
    private val dbRef = FirebaseDatabase.getInstance().getReference("users")

    override fun updatePassword(current: String, newPass: String, confirm: String) {
        if (current.isEmpty() || newPass.isEmpty() || confirm.isEmpty()) {
            view.showError("Please fill in all fields.")
            return
        }

        if (newPass.length < 6) {
            view.showError("New password must be at least 6 characters.")
            return
        }

        if (newPass != confirm) {
            view.showError("New passwords do not match.")
            return
        }

        val user = auth.currentUser
        if (user == null) {
            view.showError("User not logged in.")
            return
        }

        // Re-authenticate user before changing password
        val email = user.email
        if (email.isNullOrEmpty()) {
            view.showError("No email found for current user.")
            return
        }

        val credential = EmailAuthProvider.getCredential(email, current)

        user.reauthenticate(credential)
            .addOnSuccessListener {
                // Now safe to update password
                user.updatePassword(newPass)
                    .addOnSuccessListener {
                        // Optionally update in Realtime Database
                        val uid = user.uid
                        dbRef.child(uid).child("password").setValue(newPass)

                        view.showSuccess("Password updated successfully.")
                    }
                    .addOnFailureListener { e ->
                        view.showError("Failed to update password: ${e.message}")
                    }
            }
            .addOnFailureListener {
                view.showError("Current password is incorrect.")
            }
    }
}
