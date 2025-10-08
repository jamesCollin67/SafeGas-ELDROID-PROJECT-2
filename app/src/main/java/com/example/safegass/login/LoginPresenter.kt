package com.example.safegass.login

import android.content.Context

class LoginPresenter(private val view: LoginContract.View) {

    fun createAccount(email: String, password: String) {
        val prefs = view.getContext().getSharedPreferences("users", Context.MODE_PRIVATE)
        if (prefs.contains(email)) {
            handleLogin(email, password)
        } else {
            prefs.edit().putString(email, password).apply()
            view.showLoginSuccess()
        }
    }

    fun handleLogin(email: String, password: String) {
        val prefs = view.getContext().getSharedPreferences("users", Context.MODE_PRIVATE)
        val storedPassword = prefs.getString(email, null)
        if (storedPassword != null && storedPassword == password) {
            view.showLoginSuccess()
        } else {
            view.showLoginError("Invalid email or password.")
        }
    }
}
