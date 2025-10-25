package com.example.safegass.register

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RegisterModel : RegisterContract.Model {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database =
        FirebaseDatabase.getInstance("https://safegasses-default-rtdb.firebaseio.com/")
    private val usersRef = database.getReference("users")

    override fun registerUser(
        firstName: String,
        lastName: String,
        email: String,
        password: String,
        callback: (Boolean, String?) -> Unit
    ) {
        // ✅ Check if email is a valid Gmail address
        if (!email.endsWith("@gmail.com")) {
            callback(false, "Please use a valid Gmail account.")
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val uid = auth.currentUser?.uid ?: return@addOnCompleteListener
                    val user = mapOf(
                        "uid" to uid,
                        "firstName" to firstName,
                        "lastName" to lastName,
                        "email" to email
                    )

                    usersRef.child(uid).setValue(user)
                        .addOnSuccessListener {
                            // ✅ Send verification email after successful registration
                            auth.currentUser?.sendEmailVerification()
                                ?.addOnCompleteListener { verifyTask ->
                                    if (verifyTask.isSuccessful) {
                                        callback(true, null)
                                    } else {
                                        callback(false, "Failed to send verification email.")
                                    }
                                }
                        }
                        .addOnFailureListener { e ->
                            callback(false, e.message)
                        }
                } else {
                    callback(false, task.exception?.message)
                }
            }
    }
}
