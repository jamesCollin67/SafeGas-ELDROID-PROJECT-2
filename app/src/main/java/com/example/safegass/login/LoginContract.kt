package com.example.safegass.login

interface LoginContract {
    interface View {
        fun showLoginSuccess()
        fun showLoginError(message: String)
    }

    interface Presenter {
        fun handleLogin(email: String, password: String)
    }
}
