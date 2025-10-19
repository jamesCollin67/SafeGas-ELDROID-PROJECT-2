package com.example.safegass.register

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RegisterModel : RegisterContract.Model {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance("https://safegasses-default-rtdb.firebaseio.com/")
    private val usersRef = database.getReference("users")

    override fun registerUser(
        fullName: String,
        email: String,
        password: String,
        callback: (Boolean, String?) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val uid = auth.currentUser?.uid ?: return@addOnCompleteListener
                    val user = mapOf(
                        "uid" to uid,
                        "fullName" to fullName,
                        "email" to email
                    )
                    usersRef.child(uid).setValue(user)
                        .addOnSuccessListener { callback(true, null) }
                        .addOnFailureListener { e -> callback(false, e.message) }
                } else {
                    callback(false, task.exception?.message)
                }
            }
    }
}
