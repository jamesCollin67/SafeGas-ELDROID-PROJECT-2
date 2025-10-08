package com.example.safegass.forgotpassword

class ForgotPasswordPresenter(private val view: ForgotPasswordContract.View) : ForgotPasswordContract.Presenter {

    override fun handleSendReset(email: String) {
        if (email.isEmpty()) {
            view.showResetError("Please enter your email address")
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            view.showResetError("Invalid email format")
        } else {
            // TODO: Implement actual reset logic (API, Firebase, etc.)
            view.showResetSuccess()
        }
    }
}
