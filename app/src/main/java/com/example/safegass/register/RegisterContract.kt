package com.example.safegass.register

interface RegisterContract {

    interface View {
        fun showRegistrationSuccess()
        fun showRegistrationError(message: String)
    }

    interface Presenter {
        fun onRegisterClicked(
            fullName: String,
            email: String,
            password: String,
            confirmPassword: String,
            agreedToTerms: Boolean
        )
    }

    interface Model {
        fun registerUser(fullName: String, email: String, password: String, callback: (Boolean, String?) -> Unit)
    }
}
