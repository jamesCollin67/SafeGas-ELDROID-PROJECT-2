package com.example.safegass.forgotpassword

interface ForgotPasswordContract {
    interface View {
        fun showResetSuccess()
        fun showResetError(message: String)
        fun navigateToLogin()
    }

    interface Presenter {
        fun handleSendReset(email: String)
    }
}
