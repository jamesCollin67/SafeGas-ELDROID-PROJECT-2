package com.example.safegass.login

import android.content.Context

interface LoginContract {
    interface View {
        fun showLoginSuccess()
        fun showLoginError(message: String)
        fun getContext(): Context
    }
}
