package com.example.safegass.login

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class LoginPresenter(private val view: LoginContract.View) : LoginContract.Presenter {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database =
        FirebaseDatabase.getInstance("https://safegasses-default-rtdb.firebaseio.com/")
    private val usersRef = database.getReference("users")

    override fun handleLogin(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser

                    if (user != null) {
                        // ✅ Check if Gmail is verified
                        if (!user.isEmailVerified) {
                            view.showLoginError("Please verify your Gmail before logging in.")
                            if (view is LoginActivity) {
                                view.showResendButton() // 👈 show the resend button
                            }
                            auth.signOut()
                            return@addOnCompleteListener
                        }

                        val uid = user.uid
                        usersRef.child(uid).get()
                            .addOnSuccessListener { snapshot ->
                                if (snapshot.exists()) {
                                    view.showLoginSuccess()
                                } else {
                                    view.showLoginError("User profile not found.")
                                }
                            }
                            .addOnFailureListener { e ->
                                view.showLoginError("Database error: ${e.message}")
                            }
                    } else {
                        view.showLoginError("Login failed: User not found.")
                    }
                } else {
                    view.showLoginError("Login failed: ${task.exception?.message}")
                }
            }
    }
}
