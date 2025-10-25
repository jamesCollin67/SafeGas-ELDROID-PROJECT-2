package com.example.safegass.forgotpassword

import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordPresenter(private val view: ForgotPasswordContract.View) : ForgotPasswordContract.Presenter {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun handleSendReset(email: String) {
        if (email.isEmpty()) {
            view.showResetError("Please enter your email address")
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            view.showResetError("Invalid email format")
            return
        }

        // ✅ Send Firebase password reset email
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    view.showResetSuccess()
                } else {
                    val message = task.exception?.message ?: "Failed to send reset link"
                    view.showResetError(message)
                }
            }
    }
}
