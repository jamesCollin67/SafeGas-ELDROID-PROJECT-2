package com.example.safegass.login

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class LoginPresenter(private val view: LoginContract.View) : LoginContract.Presenter {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance("https://safegasses-default-rtdb.firebaseio.com/")
    private val usersRef = database.getReference("users")

    override fun handleLogin(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val uid = auth.currentUser?.uid
                    if (uid != null) {
                        // ✅ Optional: check if user profile exists in DB
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
                        view.showLoginError("Login failed: UID not found.")
                    }
                } else {
                    view.showLoginError("Login failed: ${task.exception?.message}")
                }
            }
    }
}
